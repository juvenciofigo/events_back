package com.providences.events.organizer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.organizer.services.GetOrganizerService;
import com.providences.events.organizer.services.GetOrganizerServiceMe;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/organizers")
public class GetOrganizerController {
    public final GetOrganizerServiceMe getOrganizerServiceMe;
    public final GetOrganizerService getOrganizerService;

    public GetOrganizerController(
            GetOrganizerServiceMe getOrganizerServiceMe,
            GetOrganizerService getOrganizerService) {
        this.getOrganizerServiceMe = getOrganizerServiceMe;
        this.getOrganizerService = getOrganizerService;
    }

    @GetMapping("/{organizerId}/me")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<OrganizerDTO.Response> getOrganizerMe(@PathVariable String organizerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getOrganizerServiceMe.execute(organizerId, userId));
    }

    @GetMapping("/{organizerId}")
    public ResponseEntity<OrganizerDTO.Response> getOrganizer(@PathVariable String organizerId) {

        return ResponseEntity.ok().body(getOrganizerService.execute(organizerId));
    }

}
