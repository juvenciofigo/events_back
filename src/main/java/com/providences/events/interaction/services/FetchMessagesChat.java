package com.providences.events.interaction.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchMessagesChat {
    private ChatRepository chatRepository;

    public FetchMessagesChat(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<MessageDTO.Response> getMessages(String chatId, String participantId, String userId) {

        ChatEntity chat = chatRepository.findByIdAndParticipants(chatId)
                .orElseThrow(() -> new BusinessException("Conversa não encontrada!", HttpStatus.NOT_FOUND));

        // Busca participante DENTRO do chat
        ParticipantChatEntity participant = chat.getParticipants().stream()
                .filter(p -> p.getId().equals(participantId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Participante não encontrado na conversa!",
                        HttpStatus.NOT_FOUND));

        // Verifica se o participantId realmente pertence ao userId logado
        boolean authorized = false;

        if (participant.getOrganizer() != null &&
                participant.getOrganizer().getUser().getId().equals(userId)) {
            authorized = true;
        }

        if (participant.getSupplier() != null &&
                participant.getSupplier().getUser().getId().equals(userId)) {
            authorized = true;
        }

        if (participant.getGuest() != null &&
                participant.getGuest().getId().equals(userId)) {
            authorized = true;
        }

        if (!authorized) {
            throw new BusinessException("Usuário não autorizado a acessar esta conversa!", HttpStatus.FORBIDDEN);
        }

        // Ordenar mensagens por data
        return chat.getMessages()
                .stream()
                .sorted(Comparator.comparing(m -> m.getCreatedAt()))
                .map(MessageDTO.Response::response)
                .collect(Collectors.toList());
    }
}
