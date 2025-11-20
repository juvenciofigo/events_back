package com.providences.events.ticket.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.TicketStatus;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class ResponseTicketsService {
    private TicketRepository ticketRepository;

    public ResponseTicketsService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public GuestDTO.Response execute(String ticketId, Boolean response) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException("Ticket not found", HttpStatus.NOT_FOUND));

        TicketStatus newStatus = response ? TicketStatus.CONFIRMED : TicketStatus.DECLINED;
        ticket.setTicketStatus(newStatus);
        TicketEntity savedTicket = ticketRepository.save(ticket);

        return GuestDTO.Response.response(savedTicket.getGuest());
    }
}
