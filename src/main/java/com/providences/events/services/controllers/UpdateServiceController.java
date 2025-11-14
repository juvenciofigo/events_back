package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.services.services.UpdateServiceService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/services")
public class UpdateServiceController {
    private UpdateServiceService updateServiceService;

    public UpdateServiceController(UpdateServiceService updateServiceService) {
        this.updateServiceService = updateServiceService;
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<ServiceDTO.Response>> execute(
            @Validated @RequestBody ServiceDTO.Update data,
            @PathVariable("serviceId") String serviceId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        ServiceDTO.Response service = updateServiceService.execute(serviceId, data, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<ServiceDTO.Response>(true, service));
    }

}