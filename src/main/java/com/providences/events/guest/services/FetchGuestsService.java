package com.providences.events.guest.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class FetchGuestsService {
    private final EventRepository eventRepository;

    public FetchGuestsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<CreateGuestDTO.Response> execute(String eventId, String userId) {

        EventEntity event = eventRepository.findIdFetchTicketsAndGuests(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        return event.getTickets().stream()
                .map(tic -> CreateGuestDTO.Response.response(tic.getGuest())).collect(Collectors.toSet());

    }
}
