package com.providences.events.ticket.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.payment.PaymentEntity.PayerType;
import com.providences.events.payment.PaymentEntity.ReceiverType;
import com.providences.events.payment.PaymentEntity.Target;
import com.providences.events.payment.dto.CreatePaymentDTO;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.Status;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class CreateTicketService {
    private final TicketRepository ticketRepository;

    // payment
    private CreatePaymentService createPaymentService;

    public CreateTicketService(TicketRepository ticketRepository, CreatePaymentService createPaymentService) {
        this.ticketRepository = ticketRepository;
        this.createPaymentService = createPaymentService;
    }

    public TicketEntity execute(EventEntity event, CreateGuestDTO.Request data, GuestEntity guest) {

        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event);
        ticket.setTotalPeople(data.getTotalPeople());
        ticket.setNotes(data.getNotes());
        ticket.setCode(UUID.randomUUID().toString());
        ticket.setSeat(data.getSeat());

        // quando o convite for pago, fazer pagamento
        if (data.isPayd == true) {
            ticket.setStatus(Status.CONFIRMED);
            ticket.setRespondedAt(LocalDateTime.now());
            CreatePaymentDTO.Request paymentData = new CreatePaymentDTO.Request();

            paymentData.setPaymentMethod(data.getPaymentMethod());
            paymentData.setPayerNum(data.getPayerNum());
            paymentData.setAmount(data.getPricePaid());

            paymentData.setPayerType(PayerType.GUEST);
            paymentData.setPayerGuest(guest);

            paymentData.setReceiverType(ReceiverType.ORGANIZER);
            paymentData.setReceiverOrganizer(event.getOrganizer());

            paymentData.setTarget(Target.TICKET);
            paymentData.setTicket(ticket);

            CreatePaymentDTO.Response paymentResponse = createPaymentService.execute(paymentData);
            System.out.println(paymentResponse);
        }

        return ticketRepository.save(ticket);
    }
}
