package com.providences.events.interaction.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.interaction.services.FetchChatMessagesService;
import com.providences.events.interaction.services.FetchMessagesService;

@RestController
@RequestMapping("/chats")
public class FetchMessagesController {
    private FetchMessagesService fetchMessagesService;
    private FetchChatMessagesService fetchChatMessagesService;

    public FetchMessagesController(
            FetchMessagesService fetchMessagesService,
            FetchChatMessagesService fetchChatMessagesService) {
        this.fetchMessagesService = fetchMessagesService;
        this.fetchChatMessagesService = fetchChatMessagesService;
    }

    @GetMapping("/messages/{organizerId}")
    public ResponseEntity<?> getMessages(
            @PathVariable String organizerId,
            @RequestParam(defaultValue = "1", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int limit,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity.ok()
                .body(fetchMessagesService.execute(organizerId, userData.getUserId(), pageNumber, limit));
    }

    @GetMapping("/{chatId}/messages/{profileId}")
    public ResponseEntity<?> getMessages(
            @PathVariable String chatId,
            @PathVariable String profileId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity.ok()
                .body(fetchChatMessagesService.execute(chatId, profileId, userData.getUserId()));
    }
}
