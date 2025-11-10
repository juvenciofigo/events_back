package com.providences.events.interaction.controllers;

import com.providences.events.interaction.dto.CreateChatDTO;
import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.services.CreateChatService;

import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

// @RestController
// @RequestMapping("/chats")
@Controller
public class CreateChatController {
    private final CreateChatService createChatService;

    public CreateChatController(CreateChatService createChatService) {
        this.createChatService = createChatService;
    }

    // @PostMapping()
    // @PreAuthorize("isAuthenticated()")
    @MessageMapping("/new-chat")
    @SendTo("/topics/livechat")
    public Set<MessageDTO.Response> postMethodName(@Validated @RequestBody CreateChatDTO.Request data) {

        return createChatService.execute(data);
    }

}