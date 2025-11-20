package com.providences.events.event.services.seats;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.repositories.TicketRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class DeleteSeatService {
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final FetchSeatsService fetchSeatsService;

    public DeleteSeatService(SeatRepository seatRepository, TicketRepository ticketRepository,
            FetchSeatsService fetchSeatsService) {
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.fetchSeatsService = fetchSeatsService;
    }

    @PersistenceContext
    private EntityManager em;

    public Set<SeatDTO.Response> execute(String seatId, String userId) {

        SeatEntity seat = seatRepository.getSeat(seatId)
                .orElseThrow(() -> new BusinessException("Assento não encontrado!", HttpStatus.BAD_REQUEST));

        EventEntity event = seat.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        Set<TicketEntity> tickets = seat.getTickets();
        if (tickets.size() > 0) {
            for (TicketEntity ticket : tickets) {
                ticket.setSeat(null);
            }
            em.flush();
        }

        seatRepository.delete(seat);

        em.flush();
        em.clear();

        Set<SeatDTO.Response> seats = fetchSeatsService.execute(event.getId(), userId);

        return seats;

    }
}
