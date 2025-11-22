package com.providences.events.plans.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.plans.dto.SubscriptionDTO;
import com.providences.events.plans.services.CreateSubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class CreateSubscriptionController {
    private CreateSubscriptionService createSubscriptionService;

    public CreateSubscriptionController(CreateSubscriptionService createSubscriptionService) {
        this.createSubscriptionService = createSubscriptionService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SubscriptionDTO.Response> execute(
            @Validated @RequestBody SubscriptionDTO.Create data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createSubscriptionService.execute(data));
    }
}
