package com.providences.events.event.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.entities.TaskEntity.TaskPriority;
import com.providences.events.event.entities.TaskEntity.TaskStatus;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.TaskRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class UpdateTaskService {
    public final TaskRepository taskRepository;
    public final EventRepository eventRepository;

    public UpdateTaskService(TaskRepository taskRepository, EventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    public TaskDTO.Response execute(String taskId, TaskDTO.Update data, String userId) {

        TaskEntity task = taskRepository.getTask(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        EventEntity event = task.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        TaskStatus taskStatus;
        TaskPriority taskPriority;
        try {
            taskStatus = TaskStatus.valueOf(data.getTaskStatus().toUpperCase());
            taskPriority = TaskPriority.valueOf(data.getPriority().toUpperCase());
        } catch (IllegalArgumentException e) {
            if (e.getMessage().toLowerCase().contains("priority")) {
                throw new BusinessException("Tipo de prioridade", HttpStatus.BAD_REQUEST);
            }
            throw new BusinessException("Tipo de status", HttpStatus.BAD_REQUEST);
        }

        task.setResponsibleName(data.getResponsibleName());
        task.setResponsiblePhone(data.getResponsiblePhone());
        task.setDescription(data.getDescription());
        task.setDueDate(data.getDueDate());
        task.setTitle(data.getTitle());
        task.setTaskStatus(taskStatus);
        task.setPriority(taskPriority);
        task.setEvent(event);
        TaskEntity taskSaved = taskRepository.save(task);

        return TaskDTO.Response.response(taskSaved);
    }
}
