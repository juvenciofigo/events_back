package com.providences.events.plans.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.plans.dto.CreateSubscriptionDTO;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.services.CreateSubscriptionService;

@RestController
@RequestMapping("/plans")
public class CreateSubscriptionController {
    private CreateSubscriptionService createSubscriptionService;

    public CreateSubscriptionController(CreateSubscriptionService createSubscriptionService) {
        this.createSubscriptionService = createSubscriptionService;
    }

    @PostMapping("/subscription")
    @PreAuthorize("hasAuthority('CLIENT')")
    public SubscriptionEntity execute(@Validated @RequestBody CreateSubscriptionDTO.Request data) {
        return createSubscriptionService.execute(data);
    }
}
