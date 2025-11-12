package com.providences.events.guest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.guest.services.UpdateGuestService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/guests")
public class UpdateGuestController {

    private UpdateGuestService updateGuestService;

    public UpdateGuestController(UpdateGuestService updateGuestService) {
        this.updateGuestService = updateGuestService;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<GuestDTO.Response>> postGuest(
            @Validated @RequestBody GuestDTO.Update data,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        GuestDTO.Response guest = updateGuestService.execute(data, UserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<GuestDTO.Response>(true, guest));
    }

}