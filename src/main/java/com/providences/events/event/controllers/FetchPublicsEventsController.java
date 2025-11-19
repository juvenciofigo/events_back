package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.services.FetchPublicEventsService;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/events")
public class FetchPublicsEventsController {
    private final FetchPublicEventsService fetchPublicEventsService;

    public FetchPublicsEventsController(FetchPublicEventsService fetchPublicEventsService) {
        this.fetchPublicEventsService = fetchPublicEventsService;
    }

    @GetMapping("/publics")
    public ResponseEntity<Set<EventDTO.Response>> getMethodName() {

        return ResponseEntity
                .ok()
                .body(fetchPublicEventsService.execute());
    }

}
