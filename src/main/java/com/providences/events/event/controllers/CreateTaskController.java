package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.CreateTaskDTO;
import com.providences.events.event.services.CreateTaskService;
import com.providences.events.shared.dto.ApiResponse;

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
    public ResponseEntity<ApiResponse<CreateTaskDTO.Response>> postMethodName(
            @Validated @RequestBody CreateTaskDTO.Request data, Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        CreateTaskDTO.Response task = createTaskService.execute(data, UserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<CreateTaskDTO.Response>(true, task));
    }

}
