package com.providences.events.ticket.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.ticket.dto.TicketDTO;
import com.providences.events.ticket.services.CheckInTicketService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/checkin")
public class CheckInTicketController {
    private final CheckInTicketService checkInTicketService;

    public CheckInTicketController(CheckInTicketService checkInTicketService) {
        this.checkInTicketService = checkInTicketService;
    }

    @PutMapping("/{ticketId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<TicketDTO.Response> putMethodName(@PathVariable String ticketId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(checkInTicketService.execute(ticketId, userId));
    }
}
