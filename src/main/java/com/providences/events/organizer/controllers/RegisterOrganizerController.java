package com.providences.events.organizer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.organizer.dto.RegisterOrganizerDTO;
import com.providences.events.organizer.services.RegisterOrganizerService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/organizers")
public class RegisterOrganizerController {
    @Autowired
    private RegisterOrganizerService registerOrganizerService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public RegisterOrganizerDTO.Response postMethodName(@Valid @RequestBody RegisterOrganizerDTO.Request dto,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserIdFromToken = userData.getUserId();
        System.out.println(UserIdFromToken);
        RegisterOrganizerDTO.Response  organizer = registerOrganizerService.execute(dto, UserIdFromToken);
        return organizer;
    }

}