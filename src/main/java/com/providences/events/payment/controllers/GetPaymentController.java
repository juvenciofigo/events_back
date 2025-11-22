package com.providences.events.payment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.services.GetPaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/payments")
public class GetPaymentController {
    private GetPaymentService getPaymentService;

    public GetPaymentController(GetPaymentService getPaymentService) {
        this.getPaymentService = getPaymentService;
    }

    @GetMapping("{paymentId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<PaymentDTO.Response> getMethodName(@PathVariable("paymentId") String paymentId,
            Authentication authentication) {
        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getPaymentService.execute(paymentId, userId));
    }

}
