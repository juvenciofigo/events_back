package com.providences.events.event.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.CreateEventDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CreateEventService {
    private OrganizerRepository organizerRepository;
    private EventRepository eventRepository;

    public CreateEventService(OrganizerRepository organizerRepository, EventRepository eventRepository) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
    }

    public CreateEventDTO.Response execute(CreateEventDTO.Request data, String UserId) {
        OrganizerEntity organizer = organizerRepository.findByUserId(UserId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizador nao encontrado"));

        EventEntity event = new EventEntity();
        event.setType(data.getType());
        event.setIsPublic(data.getIsPublic());
        event.setTitle(data.getTitle());
        event.setDateStart(data.getDateStart());
        event.setDateEnd(data.getDateEnd());
        event.setCoverImage(data.getCoverImage());
        event.setDescription(data.getDescription());
        event.setEstimatedGuest(data.getEstimatedGuest());
        event.setBudgetEstimated(data.getBudgetEstimated());
        event.setBudgetSpent(data.getBudgetSpent());
        event.setOrganizer(organizer);

        EventEntity savedEvent = eventRepository.save(event);

        return CreateEventDTO.Response.builder()
                .id(savedEvent.getId())
                .type(savedEvent.getType())
                .isPublic(savedEvent.getIsPublic())
                .title(savedEvent.getTitle())
                .dateStart(savedEvent.getDateStart())
                .dateEnd(savedEvent.getDateEnd())
                .coverImage(savedEvent.getCoverImage())
                .description(savedEvent.getDescription())
                .estimatedGuest(savedEvent.getEstimatedGuest())
                .budgetSpent(savedEvent.getBudgetSpent())
                .budgetEstimated(savedEvent.getBudgetEstimated())
                .build();

    }
}
