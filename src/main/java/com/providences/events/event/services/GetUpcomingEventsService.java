package com.providences.events.event.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.organizer.dto.DashboardOrganizerDTO;

@Service
@Transactional(readOnly = true)
public class GetUpcomingEventsService {
    private FetchEventsService fetchEventsService;

    public GetUpcomingEventsService(FetchEventsService fetchEventsService) {
        this.fetchEventsService = fetchEventsService;
    }

    public List<DashboardOrganizerDTO.upcomingEvent> execute(String organizerId, String userId, int limit, int pageNumber,
            String sort) {
        Set<EventDTO.Response> events = fetchEventsService.execute(organizerId, userId);

        return events.stream()
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
