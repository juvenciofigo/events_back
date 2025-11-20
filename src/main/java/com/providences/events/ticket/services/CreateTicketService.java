package com.providences.events.ticket.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.payment.PaymentEntity.PayerType;
import com.providences.events.payment.PaymentEntity.ReceiverType;
import com.providences.events.payment.PaymentEntity.Target;
import com.providences.events.payment.dto.CreatePaymentDTO;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.TicketStatus;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class CreateTicketService {

    private final TicketRepository ticketRepository;

    // seat
    private final SeatRepository seatRepository;

    // payment
    private CreatePaymentService createPaymentService;

    public CreateTicketService(TicketRepository ticketRepository, SeatRepository seatRepository,
            CreatePaymentService createPaymentService) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.createPaymentService = createPaymentService;
    }

    public TicketEntity execute(EventEntity event, GuestDTO.Create data, GuestEntity guest) {

        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event);
        ticket.setTotalPeople(data.getTotalPeople());
        ticket.setNotes(data.getNotes());
        ticket.setTicketCode(generateCode());
        ticket.setAccessToken(UUID.randomUUID().toString());

        // adiconar o ticket à um assento

        if (!data.getSeatId().isBlank()) {
            SeatEntity seat = event.getSeats().stream().filter(s -> s.getId().equals(data.getSeatId())).findFirst()
                    .orElseThrow(() ->  new ResourceNotFoundException("Assento não encontrado!"));

            if (seat.getAvailableSeats() != null) {

                if (seat.getAvailableSeats() >= data.getTotalPeople()
                        && seat.getAvailableSeats() - data.getTotalPeople() >= 0) {

                    seat.setAvailableSeats(seat.getAvailableSeats() - data.getTotalPeople());
                } else {
                    throw new BusinessException(
                            "Assentos insuficientes! Existem apenas "
                                    + seat.getAvailableSeats() + " assentos disponíveis.",
                            HttpStatus.BAD_REQUEST);
                }
            }

            // quando o seat for pago, fazer pagamento
            if (Boolean.TRUE.equals(seat.getIsPaid())) {
                ticket.setTicketStatus(TicketStatus.CONFIRMED);
                ticket.setRespondedAt(LocalDateTime.now());

                CreatePaymentDTO.Request paymentData = new CreatePaymentDTO.Request();

                paymentData.setPaymentMethod(data.getPaymentMethod());
                paymentData.setPayerNum(data.getPayerNum());
                paymentData.setAmount(seat.getPrice());

                paymentData.setPayerType(PayerType.GUEST);
                paymentData.setPayerGuest(guest);

                paymentData.setReceiverType(ReceiverType.ORGANIZER);
                paymentData.setReceiverOrganizer(event.getOrganizer());

                paymentData.setTarget(Target.SEAT);
                paymentData.setSeat(seat);

                createPaymentService.execute(paymentData);
            }

            // graval alterações no seat
            seatRepository.save(seat);
            ticket.setSeat(seat);

        }
        // String publicUrl = "https://yourdomain.com/public/tickets/" + ticket.getAccessToken();
        return ticketRepository.save(ticket);
    }

    private String generateCode() {
        return "TCKT-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();
    }
}
