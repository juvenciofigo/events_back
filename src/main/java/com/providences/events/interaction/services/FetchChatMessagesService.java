package com.providences.events.interaction.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.repositories.ChatRepository;

@Service
@Transactional
public class FetchChatMessagesService {
    private ChatRepository chatRepository;

    public FetchChatMessagesService(
            ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<MessageDTO.Response> execute(String chatId, String profileId, String userId) {

        ChatEntity chat = chatRepository.findByChatIdAndParticipant(chatId, profileId)
                .orElseThrow(() -> new RuntimeException("Chat not found or access denied"));

        return chat.getMessages().stream()
                .sorted((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .map(MessageDTO.Response::response)
                .toList();
    }
}
