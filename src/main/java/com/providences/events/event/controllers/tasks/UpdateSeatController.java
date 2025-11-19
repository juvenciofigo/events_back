package com.providences.events.event.controllers.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.services.seats.UpdateSeatService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/seats")
public class UpdateSeatController {
    private final UpdateSeatService updateSeatService;

    public UpdateSeatController(UpdateSeatService updateSeatService) {
        this.updateSeatService = updateSeatService;
    }

    @PutMapping("/{seatId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SeatDTO.Response> updateSeat(
            @Validated @RequestBody SeatDTO.Create data,
            @PathVariable("seatId") String seatId,
            Authentication authentication) {
        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity
                .ok()
                .body(updateSeatService.execute(seatId, data, userData.getUserId()));
    }

}
