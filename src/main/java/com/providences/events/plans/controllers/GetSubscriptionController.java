package com.providences.events.plans.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.plans.dto.SubscriptionDTO;
import com.providences.events.plans.services.GetSubscriptionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/subscriptions")
public class GetSubscriptionController {
    private GetSubscriptionService getSubscriptionService;

    public GetSubscriptionController(GetSubscriptionService getSubscriptionService) {
        this.getSubscriptionService = getSubscriptionService;
    }

    @GetMapping("/{subscriptionId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SubscriptionDTO.Response> getMethodName(
            @PathVariable String subscriptionId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getSubscriptionService.execute(subscriptionId, userId));
    }

}
