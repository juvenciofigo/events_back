package com.providences.events.interaction.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.guest.GuestEntity;
import com.providences.events.interaction.dto.MessageDTO;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.MessageEntity;
import com.providences.events.interaction.entities.MessageEntity.SenderType;
import com.providences.events.interaction.repositories.ChatRepository;
import com.providences.events.interaction.repositories.MessageRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;

@Service
@Transactional
public class CreateMessageService {
    private final MessageRepository messageRepository;

    private final ChatRepository chatRepository;

    public CreateMessageService(
            MessageRepository messageRepository,
            ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public  List<MessageDTO.Response> execute(MessageDTO.Request data) {
        SenderType senderType;
        try {
            senderType = SenderType.valueOf(data.getSender().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo de chat inválido", HttpStatus.BAD_REQUEST);
        }

        ChatEntity chat = chatRepository.findByIdAndParticipants(data.getChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada!"));

        MessageEntity message = new MessageEntity();
        message.setChat(chat);
        message.setContent(data.getContent());
        message.setSender(senderType);

        switch (senderType.name()) {
            case "GUEST" -> {
                if (data.getSenderGuestId() == null || data.getSenderGuestId().isBlank()) {
                    throw new BusinessException("Informe o convidado", HttpStatus.BAD_REQUEST);
                }

                GuestEntity guestExist = chat.getParticipants().stream()
                        .filter(participant -> participant.getGuest() != null
                                && participant.getGuest().getId().equals(data.getSenderGuestId()))
                        .map(participant -> participant.getGuest())
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Esse Guest não pertence a essa conversa!"));

                message.setSenderGuest(guestExist);

            }

            case "ORGANIZER" -> {
                if (data.getSenderOrganizerId() == null || data.getSenderOrganizerId().isBlank()) {
                    throw new BusinessException("Informe o organizador", HttpStatus.BAD_REQUEST);
                }

                OrganizerEntity organizerExist = chat.getParticipants().stream()
                        .filter(participant -> participant.getId().equals(data.getSenderOrganizerId()))
                        .map(participant -> participant.getOrganizer())
                        .findFirst()
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Esse Organizer não pertence a essa conversa!"));

                message.setSenderOrganizer(organizerExist);

            }

            case "SUPPLIER" -> {
                if (data.getSenderSupplierId() == null || data.getSenderSupplierId().isBlank()) {
                    throw new BusinessException("Informe o fornecedor", HttpStatus.BAD_REQUEST);
                }
                SupplierEntity supplierExist = chat.getParticipants().stream()
                        .filter(participant -> participant.getId().equals(data.getSenderSupplierId()))
                        .map(participant -> participant.getSupplier())
                        .findFirst()
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Esse Fornecedor não pertence a essa conversa!"));

                message.setSenderSupplier(supplierExist);

            }
            default -> throw new BusinessException("informa o remetente", HttpStatus.BAD_REQUEST);
        }
        messageRepository.save(message);
        
        List<MessageDTO.Response> messages = messageRepository.findByChatId(data.getChatId())
        .stream()
        .map(me-> MessageDTO.Response.response(me)).collect(Collectors.toList());

        return messages;
    }
}