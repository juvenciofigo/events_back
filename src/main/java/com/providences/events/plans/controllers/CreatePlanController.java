package com.providences.events.plans.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.plans.dto.CreatePlanDTO;
import com.providences.events.plans.services.CreateAddonPlanService;
import com.providences.events.plans.services.CreateOrganizerPlanService;
import com.providences.events.plans.services.CreateSupplierPlanService;

@RestController
@RequestMapping("/plans")
public class CreatePlanController {

    private final CreateOrganizerPlanService createOrganizerPlanService;
    private final CreateAddonPlanService createAddonPlanService;
    private final CreateSupplierPlanService createSupplierPlanService;

    public CreatePlanController(CreateOrganizerPlanService createOrganizerPlanService,
            CreateAddonPlanService createAddonPlanService, CreateSupplierPlanService createSupplierPlanService) {
        this.createOrganizerPlanService = createOrganizerPlanService;
        this.createAddonPlanService = createAddonPlanService;
        this.createSupplierPlanService = createSupplierPlanService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createPlan(
            @Validated @RequestBody CreatePlanDTO.Request data) {

        Object response;
        switch (data.getPlanType()) {
            case "organizer":
                response = createOrganizerPlanService.execute(data);
                break;
            case "supplier":
                response = createSupplierPlanService.execute(data);
                break;
            case "addon":
                response = createAddonPlanService.execute(data);
                break;
            default:
                throw new IllegalArgumentException("Tipo de plano inválido: " + data.getPlanType());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}