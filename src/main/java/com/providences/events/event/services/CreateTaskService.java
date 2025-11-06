package com.providences.events.event.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.CreateTaskDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.TaskRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CreateTaskService {
    public final TaskRepository taskRepository;
    public final EventRepository eventRepository;

    public CreateTaskService(TaskRepository taskRepository, EventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    public CreateTaskDTO.Response execute(CreateTaskDTO.Request data, String userId) {
        EventEntity event = eventRepository.findById(data.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }
        TaskEntity task = new TaskEntity();
        task.setResponsibleName(data.getResponsibleName());
        task.setResponsiblePhone(data.getResponsiblePhone());
        task.setDescription(data.getDescription());
        task.setDueDate(data.getDueDate());
        task.setTitle(data.getTitle());
        task.setEvent(event);

        TaskEntity taskSaved = taskRepository.save(task);
        CreateTaskDTO.Response responseDTO = CreateTaskDTO.Response.response(taskSaved);

        return responseDTO;
    }
}
