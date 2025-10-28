package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.services.dto.ServicesBySupplierDTO;
import com.providences.events.services.services.GetServicesBySupplierService;
import com.providences.events.shared.dto.ApiResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/services")
public class GetServicesBySupplierController {
    private GetServicesBySupplierService getServicesBySupplierService;

    public GetServicesBySupplierController(GetServicesBySupplierService getServicesBySupplierService) {
        this.getServicesBySupplierService = getServicesBySupplierService;
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<ApiResponse<List<ServicesBySupplierDTO>>> execute(@PathVariable String supplierId) {

        List<ServicesBySupplierDTO> services = getServicesBySupplierService.execute(supplierId);

        return ResponseEntity.ok(new ApiResponse<List<ServicesBySupplierDTO>>(true, services));
    }

}