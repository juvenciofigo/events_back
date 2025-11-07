package com.providences.events.interaction.services;

import java.util.List;
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
import com.providences.events.interaction.dto.ParticipantChatDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ChatEntity.ChatType;
import com.providences.events.interaction.entities.ParticipantChatEntity.ParticipantType;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.interaction.repositories.ParticipantChatRepository;
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
    private final ParticipantChatRepository participantChatRepository;

    public CreateChatService(
            ChatRepository chatRepository,
            EventRepository eventRepository,
            OrganizerRepository organizerRepository,
            AddParticipantToChat addParticipantToChat,
            GuestRepository guestRepository,
            SupplierRepository supplierRepository, ParticipantChatRepository participantChatRepository) {
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.addParticipantToChat = addParticipantToChat;
        this.guestRepository = guestRepository;
        this.supplierRepository = supplierRepository;
        this.participantChatRepository = participantChatRepository;
    }

    public CreateChatDTO.Response execute(CreateChatDTO.Request data) {

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
                    return CreateChatDTO.Response.response(existChat.get());
                }

                chat = chatRepository.save(chat);

                List<GuestEntity> guests = guestRepository.findGuestEvent(event.getId());

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
                    return CreateChatDTO.Response.response(existChat.get());
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
                    return CreateChatDTO.Response.response(existChat.get());
                }

                SupplierEntity supplier = supplierRepository.findById(data.getSupplierId())
                        .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

                chat = chatRepository.save(chat);
                addParticipantToChat.addParticipant(chat, ParticipantType.SUPPLIER, null,
                        null, supplier);

            }
            default -> throw new BusinessException("Unexpected value: " + chatType, HttpStatus.BAD_REQUEST);
        }

        List<ParticipantChatDTO.Response> participants = participantChatRepository.findByChatId(chat.getId()).stream()
                .map(p -> ParticipantChatDTO.Response.response(p)).collect(Collectors.toList());

        System.out.println(participants.size());

        return CreateChatDTO.Response.response(chat, participants);

    }

}
