package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.services.dto.RegisterServiceDTO;
import com.providences.events.services.services.RegisterServiceService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RegisterServiceDTO.Response>> execute(
            @Validated @RequestBody RegisterServiceDTO.Request dto) {

        RegisterServiceDTO.Response service = registerServiceService.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<RegisterServiceDTO.Response>(true, service));
    }

}