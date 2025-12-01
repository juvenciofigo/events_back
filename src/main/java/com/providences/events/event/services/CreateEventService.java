package com.providences.events.event.services;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.providences.events.config.UploadService;
import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CreateEventService {
    private OrganizerRepository organizerRepository;
    private EventRepository eventRepository;
    private UploadService uploadService;

    public CreateEventService(OrganizerRepository organizerRepository, EventRepository eventRepository,
            UploadService uploadService) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
        this.uploadService = uploadService;
    }

    public EventDTO.Response execute(EventDTO.Create data, MultipartFile file, String userId) {
        OrganizerEntity organizer = organizerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizador nao encontrado"));

        String url = "";
        if (file != null && !file.isEmpty()) {
            try {
                url = uploadService.uploadSingle(file);
            } catch (IOException e) {
                throw new BusinessException(
                        "Erro ao fazer upload: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        EventEntity event = new EventEntity();
        event.setCategory(data.getCategory());
        event.setIsPublic(data.getIsPublic());
        event.setTitle(data.getTitle());
        event.setDateStart(data.getDateStart());
        event.setDateEnd(data.getDateEnd());
        event.setCoverImage(url);
        event.setDescription(data.getDescription());
        event.setEstimatedGuest(data.getEstimatedGuest());
        event.setBudgetEstimated(data.getBudgetEstimated());
        event.setBudgetSpent(data.getBudgetSpent());
        event.setOrganizer(organizer);

        EventEntity savedEvent = eventRepository.save(event);

        return EventDTO.Response.response(savedEvent);

    }
}
