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
import com.providences.events.event.services.UpdateTaskService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tasks")
public class UpdateTaskController {
    public UpdateTaskService updateTaskService;

    public UpdateTaskController(UpdateTaskService updateTaskService) {
        this.updateTaskService = updateTaskService;
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<TaskDTO.Response>> postMethodName(
            @Validated @RequestBody TaskDTO.Update data,
            @PathVariable("taskId") String taskId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        TaskDTO.Response task = updateTaskService.execute(taskId, data, UserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<TaskDTO.Response>(true, task));
    }

}
