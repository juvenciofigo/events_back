package com.providences.events.plans.controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.plans.dto.PlanDTO;
import com.providences.events.plans.services.FetchPlansService;

@RestController
@RequestMapping("/plans")
public class FetchPlansController {
    private FetchPlansService fetchPlansService;

    public FetchPlansController(FetchPlansService fetchPlansService) {
        this.fetchPlansService = fetchPlansService;
    }


    @GetMapping("/{planType}")
     public ResponseEntity<Set<PlanDTO.Response>> execute(
            @Validated @PathVariable("planType") String planType) {
        return ResponseEntity
                .ok()
                .body(fetchPlansService.execute(planType));
    }
}
