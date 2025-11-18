package com.providences.events.supplier.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.supplier.dto.SupplierDTO;
import com.providences.events.supplier.services.RegisterSupplierService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/suppliers")
public class RegisterSupplierController {
    private RegisterSupplierService registerSupplierService;

    public RegisterSupplierController(RegisterSupplierService registerSupplierService) {
        this.registerSupplierService = registerSupplierService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SupplierDTO.Response> postMethodName(
            @Validated @RequestBody SupplierDTO.Create data,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        SupplierDTO.Response supplier = registerSupplierService.execute(data, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supplier);
    }

}