package com.providences.events.interaction.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.services.CreateMessageService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/messages")
public class CreateMessageController {
    private CreateMessageService createMessageService;

    public CreateMessageController(CreateMessageService createMessageService) {
        this.createMessageService = createMessageService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageDTO.Response>> postMessage(
            @Validated @RequestBody MessageDTO.Request data) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createMessageService.execute(data));
    }

}
