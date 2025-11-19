package com.providences.events.organizer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.organizer.services.GetOrganizerService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/organizers")
public class GetOrganizerController {
    public final GetOrganizerService getOrganizerService;

    public GetOrganizerController(GetOrganizerService getOrganizerService) {
        this.getOrganizerService = getOrganizerService;
    }

    @GetMapping("/{organizerId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<OrganizerDTO.Response> getMethodName(@PathVariable String organizerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getOrganizerService.execute(organizerId, userId));
    }

}
