package com.providences.events.event.controllers.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.TaskDTO;
import com.providences.events.event.services.tasks.GetTaskService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
public class GetTaskController {
    public GetTaskService getTaskService;

    public GetTaskController(GetTaskService getTaskService) {
        this.getTaskService = getTaskService;
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<TaskDTO.Response> getTask(
            @PathVariable("taskId") String taskId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        TaskDTO.Response task = getTaskService.execute(taskId, userId);
        return ResponseEntity.ok().body(task);
    }

}
