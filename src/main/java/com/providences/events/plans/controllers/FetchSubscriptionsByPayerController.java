package com.providences.events.plans.controllers;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.plans.dto.SubscriptionDTO;
import com.providences.events.plans.services.FetchSubscriptionsByPayerService;

@RestController
@RequestMapping("/subscriptions")
public class FetchSubscriptionsByPayerController {
    private FetchSubscriptionsByPayerService fetchSubscriptionsByPayerService;

    public FetchSubscriptionsByPayerController(FetchSubscriptionsByPayerService fetchSubscriptionsByPayerService) {
        this.fetchSubscriptionsByPayerService = fetchSubscriptionsByPayerService;
    }

    @GetMapping("/payer/{payerId}/{payerType}")
    public Set<SubscriptionDTO.Response> fetchSubscriptionsByPayer(
            @PathVariable String payerType,
            @PathVariable String payerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return fetchSubscriptionsByPayerService.execute(payerType, userId, payerId);
    }
}
