package com.providences.events.supplier.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.shared.dto.ApiResponse;
import com.providences.events.supplier.dto.RegisterSupplierDTO;
import com.providences.events.supplier.services.RegisterSupplierService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/suppliers")
public class RegisterSupplierController {
    private RegisterSupplierService registerSupplierService;

    public RegisterSupplierController(RegisterSupplierService registerSupplierService) {
        this.registerSupplierService = registerSupplierService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RegisterSupplierDTO.Response>> postMethodName(
            @Valid @RequestBody RegisterSupplierDTO.Request data,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String UserIdFromToken = userData.getUserId();

        RegisterSupplierDTO.Response supplier = registerSupplierService.execute(data, UserIdFromToken);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<RegisterSupplierDTO.Response>(true, supplier));
    }

}