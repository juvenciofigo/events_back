package com.providences.events.event.controllers.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.services.tasks.FetchTasksService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
public class FetchTaskController {
    public FetchTasksService fetchTasksService;

    public FetchTaskController(FetchTasksService fetchTasksService) {
        this.fetchTasksService = fetchTasksService;
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Set<TaskDTO.Response>> postMethodName(
            @PathVariable("eventId") String eventId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        Set<TaskDTO.Response> task = fetchTasksService.execute(eventId, userId);
        return ResponseEntity.ok().body(task);
    }

}
