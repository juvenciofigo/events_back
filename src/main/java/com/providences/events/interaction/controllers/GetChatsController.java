package com.providences.events.interaction.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.JWTUserData;
import com.providences.events.interaction.dto.GetChatDTO;
import com.providences.events.interaction.services.GetChatsService;
import com.providences.events.shared.dto.ApiResponse;

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
    public ResponseEntity<ApiResponse<Set<GetChatDTO.Response>>> getMethodName(
            @PathVariable String type,
            @RequestParam(required = false) String eventId,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String guestId,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return ResponseEntity
                .ok()
                .body(new ApiResponse<Set<GetChatDTO.Response>>(true, getChatsService.execute(
                        userData.getUserId(),
                        type,
                        eventId,
                        supplierId,
                        guestId)));

    }

}
