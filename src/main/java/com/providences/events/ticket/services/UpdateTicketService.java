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

        SeatEntity oldSeat = ticket.getSeat();
        SeatEntity newSeat;

        int oldPeople = ticket.getTotalPeople();
        int newPeople = data.getTotalPeople();

        if (oldSeat.getId().equals(data.getSeatId())) {
            newSeat = oldSeat;
        } else {
            newSeat = seatRepository.findById(data.getSeatId())
                    .orElseThrow(() -> new BusinessException("Assento não encontrado!", HttpStatus.NOT_FOUND));

            // devolve lugares do ticket antigo ao assento antigo
            oldSeat.setAvailableSeats(oldSeat.getAvailableSeats() + oldPeople);
        }

        // cálculo de diferença
        int diff = newPeople - oldPeople;

        if (diff > 0) {
            // precisa de mais lugares
            if (newSeat.getAvailableSeats() < diff) {
                throw new BusinessException(
                        "Assentos insuficientes! Existem apenas "
                                + newSeat.getAvailableSeats() + " assentos disponíveis.",
                        HttpStatus.BAD_REQUEST);
            }
            newSeat.setAvailableSeats(newSeat.getAvailableSeats() - diff);

        } else if (diff < 0) {
            // devolve lugares
            newSeat.setAvailableSeats(newSeat.getAvailableSeats() + Math.abs(diff));
        }

        // atualiza ticket
        ticket.setTotalPeople(newPeople);
        ticket.setNotes(data.getNotes());
        ticket.setSeat(newSeat);

        seatRepository.save(oldSeat);
        seatRepository.save(newSeat);

        return ticketRepository.save(ticket);
    }

}
