package com.providences.events.ticket.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.payment.PaymentEntity.PayerType;
import com.providences.events.payment.PaymentEntity.ReceiverType;
import com.providences.events.payment.PaymentEntity.Target;
import com.providences.events.payment.dto.CreatePaymentDTO;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.Status;
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

    public TicketEntity execute(EventEntity event, CreateGuestDTO.Request data, GuestEntity guest) {

        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event);
        ticket.setTotalPeople(data.getTotalPeople());
        ticket.setNotes(data.getNotes());
        ticket.setCode(UUID.randomUUID().toString());

        // adiconar o ticket à um assento

        if (!data.getSeatId().isBlank()) {
            SeatEntity seat = seatRepository.findById(data.getSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assento não encontrado!"));

            if (seat.getAvailableSeats() != null) {

                if (seat.getAvailableSeats() >= data.getTotalPeople()
                        && seat.getAvailableSeats() - data.getTotalPeople() >= 0) {

                    seat.setAvailableSeats(seat.getAvailableSeats() - data.getTotalPeople());
                } else {
                    throw new BusinessException(
                            "Assentos insuficientes! Existem apenas "
                                    + seat.getAvailableSeats() + " assentos disponíveis.");
                }
            }
            

            // quando o seat for pago, fazer pagamento
            if (Boolean.TRUE.equals(seat.getIsPaid())) {
                ticket.setStatus(Status.CONFIRMED);
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

                CreatePaymentDTO.Response paymentResponse = createPaymentService.execute(paymentData);
                System.out.println(paymentResponse);
            }

            // graval alterações no seat
            seatRepository.save(seat);
            ticket.setSeat(seat);

            System.out.println(seat);
        }

        return ticketRepository.save(ticket);
    }
}
