package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.services.services.GetServiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/services")
public class GetServiceController {
    private GetServiceService getServiceService;

    public GetServiceController(GetServiceService getServiceService) {
        this.getServiceService = getServiceService;
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDTO.Response> execute(
            @PathVariable(required = true, value = "serviceId") String serviceId,
            Authentication authentication) {

        String userId;

        if (authentication != null) {
            JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
            userId = userData.getUserId();
        } else {
            userId = null;

        }

        return ResponseEntity.ok()
                .body(getServiceService.execute(serviceId, userId));
    }

}