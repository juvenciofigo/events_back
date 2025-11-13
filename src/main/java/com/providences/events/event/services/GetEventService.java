package com.providences.events.event.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class GetEventService {
    private EventRepository eventRepository;

    public GetEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventDTO.Response execute(String eventId) {
        if (eventId.isBlank() || eventId == null) {
            throw new ResourceNotFoundException("Informe o eventId!");
        }

        EventEntity event = eventRepository.createGuest(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        return EventDTO.Response.response(eventRepository.save(event));

    }

}