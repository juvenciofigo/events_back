package com.providences.events.guest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.guest.services.CreateGuestService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CreateGuestDTO.Response>> postGuest(
            @Validated @RequestBody CreateGuestDTO.Request data,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        CreateGuestDTO.Response guest = createGuestService.execute(data, UserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<CreateGuestDTO.Response>(true, guest));
    }

}