package com.providences.events.ticket.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.ticket.dto.CreateTicketDTO;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.entities.TicketEntity.TicketStatus;
import com.providences.events.ticket.repositories.TicketRepository;

@Service
@Transactional
public class CheckInTicketService {
    private final TicketRepository ticketRepository;

    public CheckInTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public CreateTicketDTO.Response execute(String ticketId, String userId) {

        TicketEntity ticket = ticketRepository.getTicketById(ticketId)
                .orElseThrow(() -> new BusinessException("não encontrado", HttpStatus.BAD_REQUEST));

        ticket.setTicketStatus(TicketStatus.CONFIRMED);
        TicketEntity saved = ticketRepository.save(ticket);

        return CreateTicketDTO.Response.response(saved);
    }

}
