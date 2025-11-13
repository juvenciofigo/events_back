package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.services.DeleteTaskService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
public class DeleteTaskController {
    public DeleteTaskService deleteTaskService;

    public DeleteTaskController(DeleteTaskService deleteTaskService) {
        this.deleteTaskService = deleteTaskService;
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<Set<TaskDTO.Response>>> postMethodName(
            @PathVariable(value = "taskId", required = true) String taskId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        Set<TaskDTO.Response> task = deleteTaskService.execute(taskId, UserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<Set<TaskDTO.Response>>(true, task));
    }

}
