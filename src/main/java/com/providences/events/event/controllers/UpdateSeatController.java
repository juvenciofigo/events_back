package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.services.UpdateSeatService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<SeatDTO.Response>> postMethodName(
            @Validated @RequestBody SeatDTO.Create data,
            @PathVariable("seatId") String seatId,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<SeatDTO.Response>(true,
                        updateSeatService.execute(seatId, data, userData.getUserId())));
    }

}
