package com.providences.events.event.controllers.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.services.tasks.FetchTasksService;
import com.providences.events.shared.dto.SystemDTO;

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
    public ResponseEntity<SystemDTO.ItemWithPage<TaskDTO.Response>> postMethodName(
            @PathVariable String eventId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(fetchTasksService.execute(eventId, userId, pageNumber, limit, sort));
    }

}
