package com.providences.events.organizer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.organizer.dto.RegisterOrganizerDTO;
import com.providences.events.organizer.services.RegisterOrganizerService;
import com.providences.events.shared.dto.ApiResponse;


import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RegisterOrganizerService registerOrganizerService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RegisterOrganizerDTO.Response>> postMethodName(
            @Validated @RequestBody RegisterOrganizerDTO.Request dto,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        RegisterOrganizerDTO.Response organizer = registerOrganizerService.execute(dto, UserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<RegisterOrganizerDTO.Response>(true, organizer));
    }

}