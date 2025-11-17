package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.services.CreateTaskService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tasks")
public class CreateTaskController {
    public CreateTaskService createTaskService;

    public CreateTaskController(CreateTaskService createTaskService) {
        this.createTaskService = createTaskService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<TaskDTO.Response> postMethodName(
            @Validated @RequestBody TaskDTO.Create data, Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        TaskDTO.Response task = createTaskService.execute(data, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

}
