package com.providences.events.event.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;

@Service
@Transactional(readOnly = true)
public class FetchPublicEventsService {
    private final EventRepository eventRepository;

    public FetchPublicEventsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<EventDTO.Response> execute() {

        Set<EventEntity> events = eventRepository.findAllPublicEvents();

        return events.stream().map(EventDTO.Response::response).collect(Collectors.toSet());
    }
}
