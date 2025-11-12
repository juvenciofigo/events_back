package com.providences.events.guest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.guest.services.GetGuestService;
import com.providences.events.shared.dto.ApiResponse;

@RestController
@RequestMapping("/guests")
public class GetGuestController {
    private GetGuestService getGuestService;

    public GetGuestController(GetGuestService getGuestService) {
        this.getGuestService = getGuestService;
    }

    @GetMapping("/{guestId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<GuestDTO.Response>> postGuest(
            @PathVariable(required = true) String guestId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserId = userData.getUserId();

        GuestDTO.Response guest = getGuestService.execute(guestId, UserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<GuestDTO.Response>(true, guest));
    }

}