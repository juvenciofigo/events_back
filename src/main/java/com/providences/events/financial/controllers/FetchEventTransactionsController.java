package com.providences.events.financial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.financial.dto.financial;
import com.providences.events.financial.services.FetchEventTransactionsService;
import com.providences.events.shared.dto.SystemDTO;

import org.springframework.security.core.Authentication;
import com.providences.events.config.token.JWTUserDTO;

@RestController
@RequestMapping("/financial")
public class FetchEventTransactionsController {

    @Autowired
    private FetchEventTransactionsService fetchEventTransactionsService;

    @GetMapping("/events/{eventId}/transactions")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SystemDTO.ItemWithPage<financial.Transaction>> handle(
            @PathVariable String eventId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        var result = fetchEventTransactionsService.execute(eventId, userData.getUserId(), page, limit);
        return ResponseEntity.ok().body(result);
    }
}
