package com.providences.events.ticket.services;

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
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.TicketStatus;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class CreateTicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public CreateTicketService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    public TicketEntity execute(EventEntity event, GuestDTO.Create data, GuestEntity guest) {

        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event);
        ticket.setTotalPeople(data.getTotalPeople());
        ticket.setNotes(data.getNotes());
        ticket.setTicketCode(generateCode());
        ticket.setAccessToken(generateAccessToken());

        // Status inicial padrão
        ticket.setTicketStatus(TicketStatus.PENDING);

        // adicionar o ticket a um assento
        if (data.getSeatId() != null && !data.getSeatId().isBlank()) {
            SeatEntity seat = event.getSeats().stream()
                    .filter(s -> s.getId().equals(data.getSeatId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Assento não encontrado!"));

            if (seat.getAvailableSeats() != null) {
                int available = seat.getAvailableSeats();
                int people = data.getTotalPeople();

                if (available >= people) {
                    seat.setAvailableSeats(available - people);
                } else {
                    throw new BusinessException(
                            "Assentos insuficientes! Existem apenas " + available + " assentos disponíveis.",
                            HttpStatus.BAD_REQUEST);
                }
            }

            // gravar alterações no seat
            seatRepository.save(seat);
            ticket.setSeat(seat);
        }

        return ticketRepository.save(ticket);
    }

    private String generateCode() {
        return "TCKT-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();
    }

    private String generateAccessToken() {
        String access;
        do {
            access = UUID.randomUUID().toString();
        } while (ticketRepository.existsByAccessToken(access));
        return access;
    }
}
