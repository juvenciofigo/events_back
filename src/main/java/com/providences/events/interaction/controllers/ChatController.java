package com.providences.events.interaction.controllers;

import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.services.CreateMessageService;

@Controller
public class ChatController {
    private CreateMessageService createMessageService;

    public ChatController(CreateMessageService createMessageService) {
        this.createMessageService = createMessageService;
    }

    @MessageMapping("/new-message")
    @SendTo("/topics/livechat")
    public Set<MessageDTO.Response> sendMessage(MessageDTO.Request message) {

        return createMessageService.execute(message);
    }
}
