package com.providences.events.interaction.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.interaction.dto.GetChatDTO;
import com.providences.events.interaction.ChatValidations;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ChatEntity.ChatType;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional(readOnly = true)
public class GetChatsService {

    private final ChatRepository chatRepository;
    private final EventRepository eventRepository;
    private final SupplierRepository supplierRepository;

    public GetChatsService(
            ChatRepository chatRepository,
            EventRepository eventRepository,
            SupplierRepository supplierRepository) {
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<GetChatDTO.Response> execute(
            String userId,
            String type,
            String eventId,
            String supplierId,
            String guestId) {

        ChatType chatType = ChatValidations.parseChatType(type);

        List<ChatEntity> chats = switch (chatType) {
            case SUPPLIER -> getSupplierChats(userId, supplierId);
            case ORGANIZER -> getOrganizerChats(eventId);
            case GUESTS -> getGuestChats(eventId, guestId);
            default -> throw new BusinessException("Tipo inválido: " + type, HttpStatus.BAD_REQUEST);
        };

        return toSortedDtoList(chats);
    }

    // VALIDATIONS

    private EventEntity findEvent(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            throw new BusinessException("EventId é obrigatório", HttpStatus.BAD_REQUEST);
        }

        return eventRepository.findId(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));
    }

    private SupplierEntity findAndValidateSupplier(String supplierId, String userId) {
        if (supplierId == null || supplierId.isBlank()) {
            throw new BusinessException("SupplierId é obrigatório", HttpStatus.BAD_REQUEST);
        }

        SupplierEntity supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado!"));

        if (!supplier.getId().equals(userId)) {
            throw new BusinessException(
                    "Fornecedor não possui permissão para visualizar os chats",
                    HttpStatus.FORBIDDEN);
        }

        return supplier;
    }

    // CHAT LOADERS

    private List<ChatEntity> getSupplierChats(String userId, String supplierId) {
        SupplierEntity supplier = findAndValidateSupplier(supplierId, userId);
        return chatRepository.findSupplierChats(supplier.getId());
    }

    private List<ChatEntity> getOrganizerChats(String eventId) {
        EventEntity event = findEvent(eventId);
        return event.getChats();
    }

    private List<ChatEntity> getGuestChats(String eventId, String guestId) {
        if (guestId == null || guestId.isBlank()) {
            throw new BusinessException("GuestId é obrigatório", HttpStatus.BAD_REQUEST);
        }

        EventEntity event = findEvent(eventId);

        return event.getChats().stream()
                .filter(chat -> chat.getType() == ChatType.GUESTS)
                .filter(chat -> chat.getParticipants().stream()
                        .anyMatch(p -> p.getId().equals(guestId)))
                .collect(Collectors.toList());
    }

    /*
     * ---------------------------------------------------
     * MAPPING
     * ---------------------------------------------------
     */

    private List<GetChatDTO.Response> toSortedDtoList(List<ChatEntity> chats) {
        return chats.stream()
                .sorted(Comparator.comparing(ChatEntity::getUpdatedAt))
                .map(GetChatDTO.Response::response)
                .collect(Collectors.toList());
    }
}
