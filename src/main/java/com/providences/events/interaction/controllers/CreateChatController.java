package com.providences.events.interaction.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.interaction.dto.CreateChatDTO;
import com.providences.events.interaction.services.CreateChatService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/chats")
public class CreateChatController {
    private final CreateChatService createChatService;

    public CreateChatController(CreateChatService createChatService) {
        this.createChatService = createChatService;
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public CreateChatDTO.Response postMethodName(@Validated @RequestBody CreateChatDTO.Request data) {

        return createChatService.execute(data);
    }

}