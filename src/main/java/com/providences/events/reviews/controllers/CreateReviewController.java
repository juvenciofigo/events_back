package com.providences.events.reviews.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.reviews.dto.CreateReviewDTO;
import com.providences.events.reviews.services.CreateReviewService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/review")
public class CreateReviewController {
    private final CreateReviewService createReviewService;

    public CreateReviewController(CreateReviewService createReviewService) {
        this.createReviewService = createReviewService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateReviewDTO.Response> postMethodName(
            @Validated @RequestBody CreateReviewDTO.Request data,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createReviewService.execute(data, userId));
    }

}
