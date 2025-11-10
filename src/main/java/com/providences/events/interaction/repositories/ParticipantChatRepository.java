package com.providences.events.interaction.repositories;


import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.interaction.entities.ParticipantChatEntity;

public interface ParticipantChatRepository extends JpaRepository<ParticipantChatEntity, String> {

    Set<ParticipantChatEntity> findByChatId(String chatId);
}
