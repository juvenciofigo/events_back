package com.providences.events.guest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.guest.services.CreateGuestService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/guests")
public class CreateGuestController {

    private CreateGuestService createGuestService;

    public CreateGuestController(CreateGuestService createGuestService) {
        this.createGuestService = createGuestService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GuestDTO.Response> postGuest(
            @Validated @RequestBody GuestDTO.Create data,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        GuestDTO.Response guest = createGuestService.execute(data, userId);

        return ResponseEntity.ok().body(guest);
    }

}