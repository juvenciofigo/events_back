package com.providences.events.supplier.controllers;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.supplier.dto.SupplierDTO;
import com.providences.events.supplier.services.GetSupplierServiceMe;
import com.providences.events.supplier.services.GetSupplierService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/suppliers")
public class GetSupplierController {
    private GetSupplierServiceMe getSupplierServiceMe;
    private GetSupplierService getSupplierService;

    public GetSupplierController(
            GetSupplierServiceMe getSupplierServiceMe,
            GetSupplierService getSupplierService) {
        this.getSupplierServiceMe = getSupplierServiceMe;
        this.getSupplierService = getSupplierService;
    }

    @GetMapping("/{supplierId}/me")
    @PreAuthorize("hasAuthority('CLIENT')")
    public SupplierDTO.Response getSupplierMe(
            @PathVariable("supplierId") String supplierId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();
        return getSupplierServiceMe.execute(supplierId, userId);
    }

    @GetMapping("/{supplierId}")
    public SupplierDTO.Response getSupplier(
            @PathVariable("supplierId") String supplierId) {
        return getSupplierService.execute(supplierId);
    }

}
