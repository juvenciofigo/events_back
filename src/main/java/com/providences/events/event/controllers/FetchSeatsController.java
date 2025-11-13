package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.services.FetchSeatsService;
import com.providences.events.shared.dto.ApiResponse;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/seats")
public class FetchSeatsController {
    private final FetchSeatsService fetchSeatsService;

    public FetchSeatsController(FetchSeatsService fetchSeatsService) {
        this.fetchSeatsService = fetchSeatsService;
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<Set<SeatDTO.Response>>> postMethodName(
            @PathVariable("eventId") String eventId,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<Set<SeatDTO.Response>>(true,
                        fetchSeatsService.execute(eventId, userData.getUserId())));
    }

}
