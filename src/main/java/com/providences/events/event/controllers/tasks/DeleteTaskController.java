package com.providences.events.event.controllers.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.services.tasks.DeleteTaskService;

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
    public ResponseEntity<?> postMethodName(
            @PathVariable(value = "taskId", required = true) String taskId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        deleteTaskService.execute(taskId, userId);
        return ResponseEntity.ok().build();
    }

}
