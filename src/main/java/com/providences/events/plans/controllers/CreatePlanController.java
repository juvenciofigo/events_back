package com.providences.events.plans.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.plans.dto.PlanDTO;
import com.providences.events.plans.services.CreatePlanService;

@RestController
@RequestMapping("/plans")
public class CreatePlanController {

    private final CreatePlanService createPlanService;

    public CreatePlanController(CreatePlanService createPlanService) {
        this.createPlanService = createPlanService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PlanDTO.Response> createPlan(
            @Validated @RequestBody PlanDTO.Create data) {

        PlanDTO.Response response = createPlanService.execute(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}