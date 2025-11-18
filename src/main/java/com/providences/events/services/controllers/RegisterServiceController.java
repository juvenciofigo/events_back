package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.services.services.RegisterServiceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/services")
public class RegisterServiceController {
    private RegisterServiceService registerServiceService;

    public RegisterServiceController(RegisterServiceService registerServiceService) {
        this.registerServiceService = registerServiceService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ServiceDTO.Response> execute(
            @Validated @RequestBody ServiceDTO.Request data,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        ServiceDTO.Response service = registerServiceService.execute(data, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service);
    }

}