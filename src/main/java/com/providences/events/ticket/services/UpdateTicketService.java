package com.providences.events.ticket.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class UpdateTicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public UpdateTicketService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    public TicketEntity execute(TicketEntity ticket, GuestDTO.Update data) {

        if (ticket.getSeat() != null) {
            SeatEntity seat = ticket.getSeat();

            Integer availableSeats = seat.getAvailableSeats();
            int currentSeats = (availableSeats != null) ? availableSeats : 0;
            int ticketPeople = ticket.getTotalPeople();
            int newPeople = data.getTotalPeople();

            int newAvailableSeats = currentSeats + ticketPeople - newPeople;

            if (newAvailableSeats >= 0) {
                seat.setAvailableSeats(newAvailableSeats);
            } else {
                throw new BusinessException(
                        "Assentos insuficientes! Existem apenas " + currentSeats + " assentos disponíveis.",
                        HttpStatus.BAD_REQUEST);
            }

            // atualiza ticket
            ticket.setTotalPeople(newPeople);
            ticket.setNotes(data.getNotes());
            ticket.setSeat(seat);

            seatRepository.save(seat);
        }

        return ticketRepository.save(ticket);
    }
}
