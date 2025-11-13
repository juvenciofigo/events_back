package com.providences.events.event.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class FetchSeatsService {
    private final EventRepository eventRepository;

    public FetchSeatsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<SeatDTO.Response> execute(String eventId, String userId) {
        EventEntity event = eventRepository.createGuest(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        Set<SeatEntity> seats = event.getSeats();

        return seats.stream().map(SeatDTO.Response::response).collect(Collectors.toSet());

    }
}
