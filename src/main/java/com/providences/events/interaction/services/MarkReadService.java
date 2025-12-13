package com.providences.events.interaction.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.interaction.dto.MarkReadDTO;
import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.entities.MessageEntity;
import com.providences.events.interaction.repositories.MessageRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class MarkReadService {

    private final MessageRepository messageRepository;

    public MarkReadService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDTO.Response> execute(MarkReadDTO.Request data) {
        if (data.getChatId() == null || data.getChatId().isBlank()) {
            throw new BusinessException("ChatId é obrigatório", HttpStatus.BAD_REQUEST);
        }
        if (data.getViewerId() == null || data.getViewerId().isBlank()) {
            throw new BusinessException("ViewerId é obrigatório", HttpStatus.BAD_REQUEST);
        }

        Set<MessageEntity> unreadMessages = messageRepository.findByChatIdAndIsReadFalse(data.getChatId());

        List<MessageEntity> messagesToUpdate = unreadMessages.stream()
                .filter(m -> {
                    // Check if the viewer is NOT the sender
                    boolean isSenderSupplier = m.getSenderSupplier() != null
                            && m.getSenderSupplier().getId().equals(data.getViewerId());
                    boolean isSenderOrganizer = m.getSenderOrganizer() != null
                            && m.getSenderOrganizer().getId().equals(data.getViewerId());
                    boolean isSenderGuest = m.getSenderGuest() != null
                            && m.getSenderGuest().getId().equals(data.getViewerId());

                    return !(isSenderSupplier || isSenderOrganizer || isSenderGuest);
                })
                .collect(Collectors.toList());

        messagesToUpdate.forEach(m -> m.setIsRead(true));

        messageRepository.saveAll(messagesToUpdate);

        // Return the updated messages so frontend can update state
        return messagesToUpdate.stream()
                .map(MessageDTO.Response::response)
                .collect(Collectors.toList());
    }
}
