package com.providences.events.guest.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.entities.TicketEntity;

@Service
@Transactional(readOnly = true)
public class FetchGuestsService {
    private final EventRepository eventRepository;

    public FetchGuestsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<CreateGuestDTO.Response> execute(String eventId, String userId) {

        EventEntity event = eventRepository.findIdFetchTickesAndGuests(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }
        
        Set<TicketEntity> tickets = new HashSet<>(event.getTickets());

        Set<CreateGuestDTO.Response> guests = tickets.stream()
                .map(tic -> CreateGuestDTO.Response.response2(tic.getGuest())).collect(Collectors.toSet());

        return guests;
    }
}
