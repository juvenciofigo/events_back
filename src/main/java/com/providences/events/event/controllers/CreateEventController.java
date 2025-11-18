package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.services.CreateEventService;

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
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<EventDTO.Response> createEvent(
            @Validated @RequestBody EventDTO.Create data, Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createEventService.execute(data, userId));
    }

}
