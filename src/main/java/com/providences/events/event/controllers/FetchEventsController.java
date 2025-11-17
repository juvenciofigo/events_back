package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.services.FetchEventsService;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/events")
public class FetchEventsController {
    private FetchEventsService fetchEventsService;

    public FetchEventsController(FetchEventsService fetchEventsService) {
        this.fetchEventsService = fetchEventsService;
    }

    @GetMapping("/organizer/{organizerId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Set<EventDTO.Response>> getMethodName(
            @PathVariable(required = true) String organizerId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity
                .ok()
                .body(fetchEventsService.execute(organizerId, userId));
    }

}