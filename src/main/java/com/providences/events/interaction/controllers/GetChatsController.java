package com.providences.events.interaction.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.interaction.dto.GetChatDTO;
import com.providences.events.interaction.services.GetChatsService;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/chats")
public class GetChatsController {
    private final GetChatsService getChatsService;

    public GetChatsController(GetChatsService getChatsService) {
        this.getChatsService = getChatsService;
    }

    @GetMapping("/{type}")
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<GetChatDTO.Response>> getChats(
            @PathVariable String type,
            @RequestParam(required = false) String eventId,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String guestId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity
                .ok()
                .body(getChatsService.execute(
                        userData.getUserId(),
                        type,
                        eventId,
                        supplierId,
                        guestId));

    }

}
