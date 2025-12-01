package com.providences.events.event.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.services.CreateEventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/events")
public class CreateEventController {
    private CreateEventService createEventService;

    public CreateEventController(CreateEventService createEventService) {
        this.createEventService = createEventService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<EventDTO.Response> createEvent(
            @RequestPart("data") String dataStr,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication) throws JsonProcessingException {
        System.out.println(file);

        ObjectMapper mapper = new ObjectMapper();
        // Register JavaTimeModule to handle LocalDateTime
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        EventDTO.Create data = mapper.readValue(dataStr, EventDTO.Create.class);

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        System.out.println(false);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createEventService.execute(data, file, userData.getUserId()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<EventDTO.Response> createEventJson(
            @RequestBody EventDTO.Create data,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createEventService.execute(data, null, userData.getUserId()));
    }

}
