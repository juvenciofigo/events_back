package com.providences.events.interaction.controllers;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.providences.events.interaction.dto.MarkReadDTO;
import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.services.CreateMessageService;
import com.providences.events.interaction.services.MarkReadService;

@Controller
public class ChatController {
    private CreateMessageService createMessageService;
    private final MarkReadService markReadService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(CreateMessageService createMessageService, MarkReadService markReadService,
            SimpMessagingTemplate messagingTemplate) {
        this.createMessageService = createMessageService;
        this.markReadService = markReadService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/new-message")
    public void sendMessage(MessageDTO.Request message) {
        var response = createMessageService.execute(message);

        // envia a mensagem somente para o chat correto
        messagingTemplate.convertAndSend(
                "/topic/chat/" + message.getChatId(),
                response);
    }

    @MessageMapping("/mark-read")
    @SendTo("/topic/livechat")
    public List<MessageDTO.Response> markRead(MarkReadDTO.Request data) {
        return markReadService.execute(data);
    }
}
