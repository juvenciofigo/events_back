package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.services.GetSeatService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/seats")
public class GetSeatController {
    private final GetSeatService getSeatService;

    public GetSeatController(GetSeatService getSeatService) {
        this.getSeatService = getSeatService;
    }

    @GetMapping("/{seatId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<SeatDTO.Response>> postMethodName(
            @PathVariable("seatId") String seatId,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<SeatDTO.Response>(true,
                        getSeatService.execute(seatId, userData.getUserId())));
    }

}
