package com.providences.events.organizer.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.organizer.services.dasboard.GetOrganizerStatsService;
import com.providences.events.organizer.services.dasboard.GetSalesOrganizerService;
import com.providences.events.organizer.services.dasboard.GetTasksOrganizerService;

@RestController
@RequestMapping("/organizers/{organizerId}")
public class DashboardOrganizerController {
    private GetOrganizerStatsService getOrganizerStatsService;
    private GetSalesOrganizerService getSalesOrganizerService;
    private GetTasksOrganizerService getTasksOrganizerService;

    public DashboardOrganizerController(
            GetOrganizerStatsService getOrganizerStatsService,
            GetSalesOrganizerService getSalesOrganizerService,
            GetTasksOrganizerService getTasksOrganizerService) {
        this.getOrganizerStatsService = getOrganizerStatsService;
        this.getSalesOrganizerService = getSalesOrganizerService;
        this.getTasksOrganizerService = getTasksOrganizerService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<DashboardOrganizerDTO.stats> getStats(
            @PathVariable("organizerId") String organizerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getOrganizerStatsService.execute(organizerId, userId));
    }

    @GetMapping("/sales")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DashboardOrganizerDTO.sales>> getEvents(
            @PathVariable("organizerId") String organizerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getSalesOrganizerService.execute(organizerId, userId));
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<DashboardOrganizerDTO.tasks>> getTasks(
            @PathVariable("organizerId") String organizerId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(getTasksOrganizerService.execute(organizerId, userId));
    }
}
