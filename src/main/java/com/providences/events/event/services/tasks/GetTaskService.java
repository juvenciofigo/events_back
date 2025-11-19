package com.providences.events.event.services.tasks;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.TaskRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class GetTaskService {
    public final TaskRepository taskRepository;
    public final EventRepository eventRepository;

    public GetTaskService(TaskRepository taskRepository, EventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    public TaskDTO.Response execute(String taskId, String userId) {

        TaskEntity task = taskRepository.getTask(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        EventEntity event = task.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        return TaskDTO.Response.response(task);
    }
}
