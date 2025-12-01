package com.providences.events.event.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class GetUpcomingEventsService {
    private OrganizerRepository organizerRepository;

    public GetUpcomingEventsService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    public List<DashboardOrganizerDTO.upcomingEvent> execute(String organizerId, String userId) {

        OrganizerEntity organizer = organizerRepository.findId(organizerId)
                .orElseThrow(() -> new BusinessException("Organizador" + organizerId + "não encontrado",
                        HttpStatus.NOT_FOUND));

        return organizer.getEvents().stream()
                .map(EventDTO.Response::response)
                .filter(event -> event.getDateStart().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(EventDTO.Response::getDateStart))
                .map(e -> new DashboardOrganizerDTO.upcomingEvent(
                        e.getId(),
                        e.getTitle(),
                        e.getDateStart().toLocalDate().toString(),
                        e.getEstimatedGuest(),
                        null))
                .collect(Collectors.toList());

    }

}
