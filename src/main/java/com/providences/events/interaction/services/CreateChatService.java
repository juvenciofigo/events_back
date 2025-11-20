package com.providences.events.interaction.services;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.interaction.dto.CreateChatDTO;
import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ChatEntity.ChatType;
import com.providences.events.interaction.entities.ParticipantChatEntity.ParticipantType;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.interaction.repositories.MessageRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional
public class CreateChatService {
    private final ChatRepository chatRepository;
    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final SupplierRepository supplierRepository;
    private final AddParticipantToChat addParticipantToChat;
    private final GuestRepository guestRepository;

    private final MessageRepository messageRepository;

    public CreateChatService(
            ChatRepository chatRepository,
            EventRepository eventRepository,
            OrganizerRepository organizerRepository,
            AddParticipantToChat addParticipantToChat,
            GuestRepository guestRepository,
            SupplierRepository supplierRepository,
            MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.addParticipantToChat = addParticipantToChat;
        this.guestRepository = guestRepository;
        this.supplierRepository = supplierRepository;
        this.messageRepository = messageRepository;
    }

    public List<MessageDTO.Response> execute(CreateChatDTO.Request data) {

        EventEntity event = eventRepository.findById(data.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        ChatType chatType;
        try {
            chatType = ChatType.valueOf(data.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo de chat inválido", HttpStatus.BAD_REQUEST);
        }

        ChatEntity chat = new ChatEntity();
        chat.setTitle(data.getTitle());
        chat.setEvent(event);
        chat.setType(chatType);

        switch (chatType) {
            case GUESTS -> {

                Optional<ChatEntity> existChat = chatRepository.findByEventAndType(event.getId(),
                        ChatType.GUESTS);

                if (existChat.isPresent()) {

                    return getMessages(existChat.get().getId());
                }

                chat = chatRepository.save(chat);

                Set<GuestEntity> guests = new HashSet<>(guestRepository.findGuestEvent(event.getId()));

                if (!guests.isEmpty()) {
                    for (GuestEntity guest : guests) {
                        addParticipantToChat.addParticipant(chat, ParticipantType.GUEST, guest, null, null);
                    }
                }

            }

            case ORGANIZER -> {
                if (data.getOrganizerId() == null || data.getOrganizerId().isBlank()) {
                    throw new BusinessException("Informe o organizador", HttpStatus.BAD_REQUEST);
                }

                Optional<ChatEntity> existChat = chatRepository.findByEventAndTypeAndParticipantOrganizer(event.getId(),
                        ChatType.ORGANIZER, data.getOrganizerId());

                if (existChat.isPresent()) {

                    return getMessages(existChat.get().getId());
                }

                OrganizerEntity organizer = organizerRepository.findById(data.getOrganizerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado"));

                chat = chatRepository.save(chat);
                addParticipantToChat.addParticipant(chat, ParticipantType.ORGANIZER, null,
                        organizer, null);

            }

            case SUPPLIER -> {
                if (data.getSupplierId() == null || data.getSupplierId().isBlank()) {
                    throw new BusinessException("Informe o fornecedor", HttpStatus.BAD_REQUEST);
                }

                Optional<ChatEntity> existChat = chatRepository.findByEventAndTypeAndParticipantSupplier(event.getId(),
                        ChatType.SUPPLIER, data.getSupplierId());

                if (existChat.isPresent()) {
                    return getMessages(existChat.get().getId());
                }

                SupplierEntity supplier = supplierRepository.findById(data.getSupplierId())
                        .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

                chat = chatRepository.save(chat);
                addParticipantToChat.addParticipant(chat, ParticipantType.SUPPLIER, null,
                        null, supplier);

            }
            default -> throw new BusinessException("Unexpected value: " + chatType, HttpStatus.BAD_REQUEST);
        }

        addParticipantToChat.addParticipant(chat, ParticipantType.ORGANIZER, null,
                event.getOrganizer(), null);

        return getMessages(chat.getId());
    }

    private List<MessageDTO.Response> getMessages(String chatId) {
        return messageRepository.findByChatId(chatId)
                .stream()
                .sorted(Comparator.comparing(m -> m.getCreatedAt()))
                .map(MessageDTO.Response::response)
                .collect(Collectors.toList());

    }

}
