package com.providences.events.ticket.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.ticket.services.ResponseTicketsService;

@RestController
@RequestMapping("/tickets/responses")
public class ResponseTicketsController {
    private final ResponseTicketsService responseTicketsService;

    public ResponseTicketsController(ResponseTicketsService responseTicketsService) {
        this.responseTicketsService = responseTicketsService;
    }

    @PutMapping("/{ticketId}/{response}")
    public ResponseEntity<GuestDTO.Response> respondToTicket(
            @PathVariable String ticketId,
            @PathVariable("response") Boolean response) {
        return ResponseEntity.ok(responseTicketsService.execute(ticketId, response));
    }

}
