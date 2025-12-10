package com.providences.events.event.services.tasks;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.TaskEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.TaskRepository;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class FetchTasksService {
    public EventRepository eventRepository;
    public TaskRepository taskRepository;

    public FetchTasksService(
            EventRepository eventRepository,
            TaskRepository taskRepository) {
        this.eventRepository = eventRepository;
        this.taskRepository = taskRepository;
    }

    public SystemDTO.ItemWithPage<TaskDTO.Response> execute(String eventId,
            String userId,
            int pageNumber,
            int limit,
            String sort) {

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("User is not authorized to access this event", HttpStatus.FORBIDDEN);
        }

        int validPage = Math.max(pageNumber - 1, 0);
        PageRequest pageRequest = PageRequest.of(validPage, limit, Sort.by(sort).descending());

        Page<TaskEntity> tasks = taskRepository.findByEventIdPageable(eventId, pageRequest);

        List<TaskDTO.Response> list = tasks.stream().map(TaskDTO.Response::response).toList();

        return new SystemDTO.ItemWithPage<>(
                list,
                tasks.getNumber(),
                tasks.getTotalPages(),
                tasks.getTotalElements());
    }
}
