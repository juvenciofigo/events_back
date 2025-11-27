package com.providences.events.guest.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.interaction.entities.ParticipantChatEntity.ParticipantType;
import com.providences.events.interaction.services.AddParticipantToChat;
import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.entities.PaymentEntity.PayerType;
import com.providences.events.payment.entities.PaymentEntity.ReceiverType;
import com.providences.events.payment.entities.PaymentEntity.Target;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.TicketStatus;
import com.providences.events.ticket.services.CreateTicketService;

@Service
@Transactional
public class CreateGuestService {
        private final CreateTicketService createTicketService;
        private final GuestRepository guestRepository;
        private final EventRepository eventRepository;
        private final AddParticipantToChat addParticipantToChat;
        private final CreatePaymentService createPaymentService;

        public CreateGuestService(CreateTicketService createTicketService, GuestRepository guestRepository,
                        EventRepository eventRepository, AddParticipantToChat addParticipantToChat,
                        CreatePaymentService createPaymentService) {
                this.createTicketService = createTicketService;
                this.guestRepository = guestRepository;
                this.eventRepository = eventRepository;
                this.addParticipantToChat = addParticipantToChat;
                this.createPaymentService = createPaymentService;
        }

        public GuestDTO.Response execute(GuestDTO.Create data, String userId) {

                // buscar evento
                EventEntity event = eventRepository.createGuest(data.getEventId())
                                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

                if (!event.getOrganizer().getUser().getId().equals(userId)) {
                        throw new ForbiddenException("Sem permissão!");
                }

                GuestEntity guest = new GuestEntity();
                guest.setName(data.getName());
                guest.setEmail(data.getEmail());
                guest.setPhone(data.getPhone());

                // Criar convite (Ticket nasce como PENDING)
                TicketEntity ticket = createTicketService.execute(event, data, guest);
                guest.setTicket(ticket);
                ticket.setGuest(guest); // Garantir bidirecionalidade

                // Gravar informacoes do ticket e guest
                GuestEntity savedGuest = guestRepository.save(guest);

                // Verificar se o assento é pago e processar pagamento
                if (ticket.getSeat() != null && Boolean.TRUE.equals(ticket.getSeat().getIsPaid())) {

                        // Validação de dados de pagamento
                        if (data.getPaymentMethod() == null || data.getPaymentMethod().isBlank()) {
                                throw new BusinessException("Método de pagamento é obrigatório para assentos pagos!",
                                                HttpStatus.BAD_REQUEST);
                        }
                        if (data.getPayerNum() == null || data.getPayerNum().isBlank()) {
                                throw new BusinessException("Número do pagador é obrigatório para assentos pagos!",
                                                HttpStatus.BAD_REQUEST);
                        }

                        PaymentDTO.Request paymentData = new PaymentDTO.Request();
                        paymentData.setPaymentMethod(data.getPaymentMethod());
                        paymentData.setPayerNum(data.getPayerNum());
                        paymentData.setAmount(ticket.getSeat().getPrice()
                                        .multiply(BigDecimal.valueOf(data.getTotalPeople())));

                        paymentData.setPayerType(PayerType.GUEST);
                        paymentData.setPayerGuest(savedGuest);

                        paymentData.setReceiverType(ReceiverType.ORGANIZER);
                        paymentData.setReceiverOrganizer(event.getOrganizer());

                        paymentData.setTarget(Target.SEAT);
                        paymentData.setSeat(ticket.getSeat());

                        // Criar pagamento - se falhar, rollback acontece
                        createPaymentService.execute(paymentData);

                        // Se pagamento sucesso -> Confirmar ticket
                        ticket.setTicketStatus(TicketStatus.CONFIRMED);
                        ticket.setRespondedAt(LocalDateTime.now());
                }

                GuestDTO.Response responseGuest = GuestDTO.Response.response(savedGuest);

                event.getChats().stream()
                                .filter(chat -> chat.getType().name().equalsIgnoreCase("guests"))
                                .findFirst()
                                .ifPresent(chat -> {
                                        addParticipantToChat.addParticipant(chat, ParticipantType.GUEST, savedGuest,
                                                        null, null);
                                });
                return responseGuest;
        }
}