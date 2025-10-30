package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.CreateEventDTO;
import com.providences.events.event.services.CreateEventService;
import com.providences.events.shared.dto.ApiResponse;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/events")
public class CreateEventController {
    private CreateEventService createEventService;

    public CreateEventController(CreateEventService createEventService) {
        this.createEventService = createEventService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CreateEventDTO.Response>> createEvent(
            @Validated @RequestBody CreateEventDTO.Request data, Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserIdFromToken = userData.getUserId();

        CreateEventDTO.Response event = createEventService.execute(data, UserIdFromToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<CreateEventDTO.Response>(true, event));
    }

}
