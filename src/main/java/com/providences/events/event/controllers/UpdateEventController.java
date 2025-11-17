package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.services.UpdateEventService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/events")
public class UpdateEventController {
    private UpdateEventService updateEventService;

    public UpdateEventController(UpdateEventService updateEventService) {
        this.updateEventService = updateEventService;
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<EventDTO.Response> createEvent(
            @Validated @RequestBody EventDTO.Update data,
            @PathVariable(required = true) String eventId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        EventDTO.Response event = updateEventService.execute(eventId, data, userId);

        return ResponseEntity
                .ok()
                .body(event);
    }

}
