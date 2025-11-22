package com.providences.events.interaction.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
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

        public List<MessageDTO.Response> execute(MessageDTO.Request data) {

                SenderType senderType = Arrays.stream(SenderType.values())
                                .filter(type -> type.name().equalsIgnoreCase(data.getSenderRole()))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException("Tipo de remetente inválido",
                                                HttpStatus.BAD_REQUEST));

                ChatEntity chat = chatRepository.findByIdAndParticipants(data.getChatId())
                                .orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada!"));

                MessageEntity message = new MessageEntity();
                message.setChat(chat);
                message.setContent(data.getContent());
                message.setSender(senderType);

                switch (senderType) {
                        case GUEST -> {

                                GuestEntity guestExist = chat.getParticipants().stream()
                                                .filter(participant -> participant.getGuest() != null
                                                                && participant.getGuest().getId()
                                                                                .equals(data.getSenderId()))
                                                .map(participant -> participant.getGuest())
                                                .findFirst()
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Esse Guest não pertence a essa conversa!"));

                                message.setSenderGuest(guestExist);

                        }

                        case ORGANIZER -> {

                                OrganizerEntity organizerExist = chat.getParticipants().stream()
                                                .filter(participant -> participant.getId().equals(data.getSenderId()))
                                                .map(participant -> participant.getOrganizer())
                                                .findFirst()
                                                .orElseThrow(
                                                                () -> new ResourceNotFoundException(
                                                                                "Esse Organizer não pertence a essa conversa!"));

                                message.setSenderOrganizer(organizerExist);

                        }

                        case SUPPLIER -> {

                                SupplierEntity supplierExist = chat.getParticipants().stream()
                                                .filter(participant -> participant.getId().equals(data.getSenderId()))
                                                .map(participant -> participant.getSupplier())
                                                .findFirst()
                                                .orElseThrow(
                                                                () -> new ResourceNotFoundException(
                                                                                "Esse Fornecedor não pertence a essa conversa!"));

                                message.setSenderSupplier(supplierExist);

                        }
                        default -> throw new BusinessException("informa o remetente", HttpStatus.BAD_REQUEST);
                }
                chat.setUpdatedAt(LocalDateTime.now());
                chatRepository.save(chat);
                messageRepository.save(message);

                List<MessageDTO.Response> messages = messageRepository.findByChatId(data.getChatId())
                                .stream()
                                .sorted(Comparator.comparing(m -> m.getCreatedAt()))
                                .map(MessageDTO.Response::response)
                                .collect(Collectors.toList());

                return messages;
        }
}