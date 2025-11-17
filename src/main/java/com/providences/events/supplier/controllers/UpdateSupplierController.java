package com.providences.events.supplier.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;

import com.providences.events.supplier.dto.SupplierDTO;
import com.providences.events.supplier.services.UpdateSupplierService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/suppliers")
public class UpdateSupplierController {
    private final UpdateSupplierService updateSupplierService;

    public UpdateSupplierController(UpdateSupplierService updateSupplierService) {
        this.updateSupplierService = updateSupplierService;
    }

    @PutMapping("/{supplierId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SupplierDTO.Response> postMethodName(
            @Validated @RequestBody SupplierDTO.Update data,
            @PathVariable(value = "supplierId", required = true) String supplierId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(updateSupplierService.execute(supplierId, data, userId));
    }

}