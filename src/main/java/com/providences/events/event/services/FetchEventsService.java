package com.providences.events.event.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.organizer.dto.DashboardOrganizerDTO.ItemWithPage;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchEventsService {
    private OrganizerRepository organizerRepository;
    private EventRepository eventRepository;

    public FetchEventsService(
            OrganizerRepository organizerRepository,
            EventRepository eventRepository) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
    }

    public ItemWithPage<EventDTO.Response> execute(String organizerId, String userId, int limit, int pageNumber,
            String sort) {

        OrganizerEntity organizer = organizerRepository.findId(organizerId)
                .orElseThrow(() -> new BusinessException("Organizador" + organizerId + "não encontrado",
                        HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("Sem permissão", HttpStatus.FORBIDDEN);
        }

        int validPage = Math.max(pageNumber - 1, 0);
        PageRequest pageRequest = PageRequest.of(validPage, limit,
                Sort.by(sort).descending());

        Page<EventEntity> events = eventRepository.findByOrganizer(organizerId, pageRequest);

        List<EventDTO.Response> list = events.stream()
                .map(e -> EventDTO.Response.response(e))
                .toList();

        return new DashboardOrganizerDTO.ItemWithPage<>(
                list,
                events.getNumber() + 1,
                events.getTotalPages(),
                events.getTotalElements());

    }

}
