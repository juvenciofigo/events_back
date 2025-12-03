package com.providences.events.guest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.guest.services.FetchGuestsService;
import com.providences.events.shared.dto.SystemDTO;

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
    public ResponseEntity<SystemDTO.ItemWithPage<GuestDTO.Response>> getGuestsByEventId(
            @PathVariable String eventId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity
                .ok()
                .body(fetchGuestsService.execute(eventId, userId, pageNumber, limit, sort));
    }

}
