package com.providences.events.event.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchEventsService {
    private OrganizerRepository organizerRepository;

    public FetchEventsService(OrganizerRepository eventRepository) {
        this.organizerRepository = eventRepository;
    }

    public Set<EventDTO.Response> execute(String organizerId, String userId) {

        OrganizerEntity organizer = organizerRepository.findId(organizerId)
                .orElseThrow(() -> new BusinessException("Organigador" + organizerId + "não encontrado",
                        HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("Sem permissão", HttpStatus.FORBIDDEN);
        }

        Set<EventDTO.Response> events = organizer.getEvents()
                .stream()
                .map(EventDTO.Response::response)
                .collect(Collectors.toSet());

        return events;

    }

}
