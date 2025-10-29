package com.providences.events.ticket.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.ticket.TicketEntity;
import com.providences.events.ticket.TicketRepository;

@Service
@Transactional
public class CreateTicketService {
    private final TicketRepository ticketRepository;

    public CreateTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketEntity execute(EventEntity event, CreateGuestDTO.Request data) {
        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event);
        ticket.setTotalPeople(data.getTotalPeople());
        ticket.setNotes(data.getNotes());
        ticket.setCode(UUID.randomUUID().toString());
        ticket.setSeat(data.getSeat());

        return ticketRepository.save(ticket);
    }
}
