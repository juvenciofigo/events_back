package com.providences.events.organizer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.organizer.dto.OrganizerStatsDTO;
import com.providences.events.organizer.services.GetOrganizerStatsService;

@RestController
@RequestMapping("/organizers")
public class GetOrganizerStatsController {
    private GetOrganizerStatsService getOrganizerStatsService;

    public GetOrganizerStatsController(GetOrganizerStatsService getOrganizerStatsService) {
        this.getOrganizerStatsService = getOrganizerStatsService;
    }

    @GetMapping("/{organizerId}/stats")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<OrganizerStatsDTO> getStats(
            @PathVariable("organizerId") String organizerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getOrganizerStatsService.execute(organizerId, userId));
    }
}
