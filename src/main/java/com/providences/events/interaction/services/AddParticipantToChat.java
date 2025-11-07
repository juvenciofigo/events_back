package com.providences.events.interaction.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.guest.GuestEntity;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity.ParticipantType;
import com.providences.events.interaction.repositories.ParticipantChatRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.supplier.SupplierEntity;

@Service
@Transactional
public class AddParticipantToChat {
    private final ParticipantChatRepository participantChatRepository;

    public AddParticipantToChat(ParticipantChatRepository participantChatRepository) {
        this.participantChatRepository = participantChatRepository;
    }

    public ParticipantChatEntity addParticipant(ChatEntity chat,ParticipantType type, GuestEntity guest, OrganizerEntity organizer,
            SupplierEntity supplier) {

        ParticipantChatEntity participant = new ParticipantChatEntity();
        participant.setChat(chat);
        participant.setType(type);
        participant.setGuest(guest);
        participant.setOrganizer(organizer);
        participant.setSupplier(supplier);
        return participantChatRepository.save(participant);

    }
}
