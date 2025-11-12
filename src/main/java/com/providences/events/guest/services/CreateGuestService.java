package com.providences.events.guest.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.interaction.entities.ParticipantChatEntity.ParticipantType;
import com.providences.events.interaction.services.AddParticipantToChat;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.services.CreateTicketService;

@Service
@Transactional
public class CreateGuestService {
        private final CreateTicketService createTicketService;
        private final GuestRepository guestRepository;
        private final EventRepository eventRepository;
        private AddParticipantToChat addParticipantToChat;

        public CreateGuestService(CreateTicketService createTicketService, GuestRepository guestRepository,
                        EventRepository eventRepository, AddParticipantToChat addParticipantToChat) {
                this.createTicketService = createTicketService;
                this.guestRepository = guestRepository;
                this.eventRepository = eventRepository;
                this.addParticipantToChat = addParticipantToChat;
        }

        public CreateGuestDTO.Response execute(CreateGuestDTO.Request data, String userId) {

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

                // Criar convite
                TicketEntity ticket = createTicketService.execute(event, data, guest);
                guest.setTicket(ticket);

                // Gravar informacoes do ticket
                GuestEntity savedGuest = guestRepository.save(guest);

                CreateGuestDTO.Response responseGuest = CreateGuestDTO.Response.response(savedGuest);

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