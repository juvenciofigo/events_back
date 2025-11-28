package com.providences.events.reviews.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.organizer.dto.DashboardOrganizerDTO.ItemWithPage;
import com.providences.events.reviews.services.FetchReviewsService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/reviews")
public class FetchReviewsController {
    private FetchReviewsService fetchReviewsService;

    public FetchReviewsController(FetchReviewsService fetchReviewsService) {
        this.fetchReviewsService = fetchReviewsService;
    }

    @GetMapping
    public ResponseEntity<ItemWithPage<DashboardOrganizerDTO.reviews>> getMethodName(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(required = true) String target,
            @RequestParam(required = true) String targetId) {

        return ResponseEntity.ok(fetchReviewsService.execute(pageNumber, limit, sort,
                target, targetId));
    }

}
