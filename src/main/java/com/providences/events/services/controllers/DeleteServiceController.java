package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.services.services.DeleteServiceService;
import com.providences.events.shared.dto.ApiResponse;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/services")
public class DeleteServiceController {
    private DeleteServiceService deleteServiceService;

    public DeleteServiceController(DeleteServiceService deleteServiceService) {
        this.deleteServiceService = deleteServiceService;
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<Set<ServiceDTO.Response>>> execute(
            @PathVariable(required = true, value = "serviceId") String serviceId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok()
                .body(new ApiResponse<Set<ServiceDTO.Response>>(
                        true,
                        deleteServiceService.execute(serviceId, userId)));
    }

}