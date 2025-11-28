package com.providences.events.organizer.services.dasboard;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class GetTasksOrganizerService {
    private OrganizerRepository organizerRepository;

    public GetTasksOrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    public List<DashboardOrganizerDTO.tasks> execute(String organizerId, String userId) {
        OrganizerEntity organizer = organizerRepository.statsOrganizer(organizerId)
                .orElseThrow(() -> new BusinessException("Organizer not found", HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to access this organizer", HttpStatus.UNAUTHORIZED);
        }

        Set<EventEntity> events = organizer.getEvents();

        List<DashboardOrganizerDTO.tasks> tasks = events.stream()
                .flatMap(e -> e.getTasks().stream())
                .map(task -> new DashboardOrganizerDTO.tasks(
                        task.getId(),
                        task.getTitle(),
                        task.getEvent().getTitle(),
                        task.getDescription(),
                        task.getPriority().name(),
                        task.getTaskStatus().name()))
                .collect(Collectors.toList());

        return tasks;

    }

}
