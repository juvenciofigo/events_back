package com.providences.events.event.services.tasks;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class FetchTasksService {
    public final EventRepository eventRepository;

    public FetchTasksService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<TaskDTO.Response> execute(String eventId, String userId) {
        EventEntity event = eventRepository.findId(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        Set<TaskEntity> tasks = event.getTasks();

        return tasks.stream().map(TaskDTO.Response::response).collect(Collectors.toSet());
    }
}
