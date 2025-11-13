package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.services.GetEventService;
import com.providences.events.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/events")
public class GetEventController {
    private GetEventService getEventService;

    public GetEventController(GetEventService getEventService) {
        this.getEventService = getEventService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventDTO.Response>> getMethodName(@PathVariable(required = true) String eventId) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<EventDTO.Response>(true, getEventService.execute(eventId)));
    }

}