package com.providences.events.payment.controllers;

import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.services.FetchPaymentsByPayerService;

@RestController
@RequestMapping("/payments")
public class FetchPaymentsByPayerController {

    private FetchPaymentsByPayerService fetchPaymentsByPayerService;

    public FetchPaymentsByPayerController(FetchPaymentsByPayerService fetchPaymentsByPayerService) {
        this.fetchPaymentsByPayerService = fetchPaymentsByPayerService;
    }

    @GetMapping("/payer/{payerId}/{payerType}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public Set<PaymentDTO.Response> fetchPaymentsByPayer(
            @PathVariable String payerType,
            @PathVariable String payerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();
        return fetchPaymentsByPayerService.execute(payerType, userId, payerId);
    }
}
