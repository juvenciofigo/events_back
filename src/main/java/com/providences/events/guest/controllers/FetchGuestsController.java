package com.providences.events.guest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.guest.services.FetchGuestsService;
import com.providences.events.shared.dto.ApiResponse;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/guests")
public class FetchGuestsController {
    private final FetchGuestsService fetchGuestsService;

    public FetchGuestsController(FetchGuestsService fetchGuestsService) {
        this.fetchGuestsService = fetchGuestsService;
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<Set<GuestDTO.Response>>> getMethodName(@PathVariable String eventId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        return ResponseEntity
                .ok()
                .body(new ApiResponse<Set<GuestDTO.Response>>(true, fetchGuestsService.execute(eventId, UserId)));
    }

}
