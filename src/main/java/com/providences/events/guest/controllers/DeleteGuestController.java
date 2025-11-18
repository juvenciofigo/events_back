package com.providences.events.guest.controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.guest.services.DeleteGuestService;

@RestController
@RequestMapping("/guests")
public class DeleteGuestController {
    private DeleteGuestService deleteGuestService;

    public DeleteGuestController(DeleteGuestService deleteGuestService) {
        this.deleteGuestService = deleteGuestService;
    }

    @DeleteMapping("/{guestId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Set<GuestDTO.Response>> delete(
            @PathVariable String guestId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity
                .ok()
                .body(deleteGuestService.execute(guestId, userData.getUserId()));
    }
}
