package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.services.CreateSeatService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/seats")
public class CreateSeatController {
    private final CreateSeatService createSeatService;

    public CreateSeatController(CreateSeatService createSeatService) {
        this.createSeatService = createSeatService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SeatDTO.Response> postMethodName(
            @Validated @RequestBody SeatDTO.Create data,
            @PathVariable("eventId") String eventId,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createSeatService.execute(eventId, data, userData.getUserId()));
    }

}
