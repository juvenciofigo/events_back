package com.providences.events.financial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.financial.dto.financial;
import com.providences.events.financial.services.GetEventFinancialStatsServices;

@RestController
@RequestMapping("/financial")
public class GetEventFinancialStatsController {

    @Autowired
    private GetEventFinancialStatsServices getEventFinancialStatsServices;

    @GetMapping("/events/{eventId}/stats")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<financial.Response> handle(
            @PathVariable String eventId,
               Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        var result = getEventFinancialStatsServices.execute(eventId, userData.getUserId());
        return ResponseEntity.ok().body(result);
    }
}
