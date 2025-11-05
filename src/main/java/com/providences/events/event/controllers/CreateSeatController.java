package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.providences.events.config.JWTUserData;
import com.providences.events.event.dto.CreataSeatDTO;
import com.providences.events.event.services.CreateSeatService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/seats")
public class CreateSeatController {
    private final CreateSeatService createSeatService;

    public CreateSeatController(CreateSeatService createSeatService) {
        this.createSeatService = createSeatService;
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public CreataSeatDTO.Response postMethodName(@Validated @RequestBody CreataSeatDTO.Request data,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return createSeatService.execute(data, userData.getUserId());
    }

}
