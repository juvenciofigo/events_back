package com.providences.events.ticket.controllers;

import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.ticket.dto.TicketDTO;
import com.providences.events.ticket.services.FetchEventTicketsService;

@RestController
@RequestMapping("/events")
public class FetchEventTicketsController {
    private FetchEventTicketsService fetchEventTicketsService;

    public FetchEventTicketsController(FetchEventTicketsService fetchEventTicketsService) {
        this.fetchEventTicketsService = fetchEventTicketsService;
    }

    @GetMapping("/{eventId}/tickets")
    @PreAuthorize("hasAuthority('CLIENT')")
    public Set<TicketDTO.Response> execute(@PathVariable(required = true) String eventId,
            Authentication authentication) {
        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return fetchEventTicketsService.execute(eventId, userId);
    }
}
