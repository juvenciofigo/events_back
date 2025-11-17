package com.providences.events.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.services.services.FetchServicesService;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/services")
public class FetchServicesController {
    private FetchServicesService fetchServicesService;

    public FetchServicesController(FetchServicesService fetchServicesService) {
        this.fetchServicesService = fetchServicesService;
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<Set<ServiceDTO.Response>> execute(@PathVariable String supplierId) {

        Set<ServiceDTO.Response> services = fetchServicesService.execute(supplierId);

        return ResponseEntity.ok().body(services);
    }

}