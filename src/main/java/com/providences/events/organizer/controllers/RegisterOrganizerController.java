package com.providences.events.organizer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.organizer.services.RegisterOrganizerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/organizers")
public class RegisterOrganizerController {
    private RegisterOrganizerService registerOrganizerService;

    public RegisterOrganizerController(RegisterOrganizerService registerOrganizerService) {
        this.registerOrganizerService = registerOrganizerService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrganizerDTO.Response> postOrganizer(
            @Validated @RequestBody OrganizerDTO.Create dto,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        OrganizerDTO.Response organizer = registerOrganizerService.execute(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizer);
    }

}