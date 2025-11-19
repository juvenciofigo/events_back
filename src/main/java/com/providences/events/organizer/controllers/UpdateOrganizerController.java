package com.providences.events.organizer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.organizer.services.UpdateOrganizerService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/organizers")
public class UpdateOrganizerController {

    private final UpdateOrganizerService updateOrganizerService;

    public UpdateOrganizerController(UpdateOrganizerService updateOrganizerService) {
        this.updateOrganizerService = updateOrganizerService;
    }

    @PutMapping("/{organizerId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<OrganizerDTO.Response> putMethodName(
            @PathVariable String organizerId,
            @RequestBody OrganizerDTO.Create data,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(updateOrganizerService.execute(organizerId, data, userId));
    }
}