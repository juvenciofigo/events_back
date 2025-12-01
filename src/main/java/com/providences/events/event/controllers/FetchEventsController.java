package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.services.FetchEventsService;
import com.providences.events.event.services.GetUpcomingEventsService;

import jakarta.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/events")
public class FetchEventsController {
    private FetchEventsService fetchEventsService;
    private GetUpcomingEventsService getUpcomingEventsService;

    public FetchEventsController(FetchEventsService fetchEventsService,
            GetUpcomingEventsService getUpcomingEventsService) {
        this.fetchEventsService = fetchEventsService;
        this.getUpcomingEventsService = getUpcomingEventsService;
    }

    @GetMapping("/organizer/{organizerId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<?> getEventsByOrganizer(
            @PathVariable(required = true) String organizerId,
            @RequestParam(required = false, defaultValue = "false") boolean upcoming,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        if (upcoming) {
            return ResponseEntity
                    .ok()
                    .body(getUpcomingEventsService.execute(organizerId, userId));
        }
        return ResponseEntity
                .ok()
                .body(fetchEventsService.execute(organizerId, userId, limit, pageNumber, sort));
    }

}