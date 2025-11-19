package com.providences.events.ticket.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class GetTicketByTokenService {
    private final TicketRepository ticketRepository;

    public GetTicketByTokenService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketEntity execute(String token) {
        TicketEntity ticket = ticketRepository.findByAccessToken(token)
                .orElseThrow(() -> new BusinessException("Ticket não encontrado!", HttpStatus.NOT_FOUND));
        return ticket;
    }
}
