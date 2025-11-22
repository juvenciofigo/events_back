package com.providences.events.plans.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.plans.dto.PlanDTO;
import com.providences.events.plans.services.UpdatePlanService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/plans")
public class UpdatePlanController {
    private final UpdatePlanService updatePlanService;

    public UpdatePlanController(UpdatePlanService updatePlanService) {
        this.updatePlanService = updatePlanService;
    }

    @PutMapping("/{planId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PlanDTO.Response> putMethodName(
            @Validated @RequestBody PlanDTO.Create data,
            @PathVariable("planId") String planId) {

        return ResponseEntity.ok().body(updatePlanService.updatePlan(planId, data));
    }
}