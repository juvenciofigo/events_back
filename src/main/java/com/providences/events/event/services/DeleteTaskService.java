package com.providences.events.event.services;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.repositories.TaskRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class DeleteTaskService {
    public final TaskRepository taskRepository;
    public FetchTasksService fetchTasksService;

    @PersistenceContext
    private EntityManager em;

    public DeleteTaskService(TaskRepository taskRepository, FetchTasksService fetchTasksService) {
        this.taskRepository = taskRepository;
        this.fetchTasksService = fetchTasksService;
    }

    public Set<TaskDTO.Response> execute(String taskId, String userId) {

        TaskEntity task = taskRepository.getTask(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        EventEntity event = task.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }
        taskRepository.delete(task);

        em.flush();
        em.clear();

        Set<TaskDTO.Response> tasks = fetchTasksService.execute(event.getId(), userId);

        return tasks;
    }
}
