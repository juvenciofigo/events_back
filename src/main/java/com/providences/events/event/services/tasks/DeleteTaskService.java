package com.providences.events.event.services.tasks;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.repositories.TaskRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class DeleteTaskService {
    public final TaskRepository taskRepository;
    public FetchTasksService fetchTasksService;

    public DeleteTaskService(TaskRepository taskRepository, FetchTasksService fetchTasksService) {
        this.taskRepository = taskRepository;
        this.fetchTasksService = fetchTasksService;
    }

    public void execute(String taskId, String userId) {

        TaskEntity task = taskRepository.getTask(taskId)
                .orElseThrow(() -> new BusinessException("Despesa nao encontrada", HttpStatus.NOT_FOUND));

        EventEntity event = task.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to delete this expense", HttpStatus.FORBIDDEN);
        }

        taskRepository.delete(task);

    }
}
