package com.providences.events.interaction.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.interaction.dto.GetChatDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ChatEntity.ChatType;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class GetChatsService {
    private ChatRepository chatRepository;
    private EventRepository eventRepository;

    public GetChatsService(ChatRepository chatRepository, EventRepository eventRepository) {
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
    }

    public Set<GetChatDTO.Response> execute(
            String userId,
            String type,
            String eventId,
            String supplierId,
            String guestId) {
        if (type == null || type.isBlank()) {
            throw new BusinessException("Tipo é obrigatório", HttpStatus.BAD_REQUEST);
        }

        Set<ChatEntity> chats;

        switch (type.toLowerCase()) {
            case "supplier":
                if (supplierId == null || supplierId.isBlank()) {
                    throw new BusinessException("SupplierId é obrigatório", HttpStatus.BAD_REQUEST);
                }
                chats = new HashSet<>(chatRepository.findSupplierChats(supplierId));
                break;

            case "organizer":

                EventEntity eventOrganizer = findEvent(eventId);

                chats = new HashSet<>(eventOrganizer.getChats());
                break;

            case "guest":

                if (guestId == null || guestId.isBlank()) {
                    throw new BusinessException("GuestId é obrigatório", HttpStatus.BAD_REQUEST);
                }
                
                EventEntity eventGuest = findEvent(eventId);

                chats = eventGuest.getChats()
                        .stream()
                        .filter(chat -> chat.getType().equals(ChatType.GUESTS))
                        .filter(chat -> chat.getParticipants()
                                .stream()
                                .anyMatch(p -> p.getId().equals(guestId)))
                        .collect(Collectors.toSet());
                ;
                break;

            default:
                throw new BusinessException("Tipo inválido: " + type, HttpStatus.BAD_REQUEST);
        }

        return chats.stream()
                .map(chat -> GetChatDTO.Response.response(chat))
                .collect(Collectors.toSet());

    }

    private EventEntity findEvent(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            throw new BusinessException("EventId é obrigatório", HttpStatus.BAD_REQUEST);
        }

        return eventRepository.findId(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));
    }
}
