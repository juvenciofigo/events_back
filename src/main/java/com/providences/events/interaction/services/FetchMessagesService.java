package com.providences.events.interaction.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.interaction.dto.MessageDTO.LastMessageDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.MessageEntity;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class FetchMessagesService {
    private OrganizerRepository organizerRepository;
    private ChatRepository chatRepository;

    public FetchMessagesService(OrganizerRepository organizerRepository, ChatRepository chatRepository) {
        this.organizerRepository = organizerRepository;
        this.chatRepository = chatRepository;
    }

    public List<LastMessageDTO> execute(String organizerId, String userId, int pageNumber, int limit) {
        OrganizerEntity organizer = organizerRepository.findById(organizerId)
                .orElseThrow(() -> new BusinessException("Organizer not found", HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("User not authorized", HttpStatus.FORBIDDEN);
        }

        Page<ChatEntity> chats = chatRepository.findByParticipant(organizerId,
                PageRequest.of(pageNumber - 1 >= 0 ? pageNumber - 1 : 0, limit, Sort.by("updatedAt").descending()));

        List<LastMessageDTO> messages = chats.getContent().stream()
                .map(chat -> chat.getMessages().stream()
                        .max(Comparator.comparing(MessageEntity::getCreatedAt))
                        .orElse(null))
                .filter(msg -> msg != null)
                .map(msg -> new LastMessageDTO(
                        msg.getId(),
                        sender(msg),
                        msg.getContent(),
                        event(msg),
                        msg.getCreatedAt(),
                        msg.getIsRead()))
                .collect(Collectors.toList());

        return messages;
    }

    private String sender(MessageEntity message) {
        switch (message.getSender()) {
            case SUPPLIER:
                return message.getSenderSupplier().getCompanyName();
            case GUEST:
                return message.getSenderGuest().getName();
            case ORGANIZER:
                return message.getSenderOrganizer().getCompanyName();
            default:
                return "";
        }
    }

    private String event(MessageEntity message) {
        return message.getChat().getEvent().getTitle();
    }
}
