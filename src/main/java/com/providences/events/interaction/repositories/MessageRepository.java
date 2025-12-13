package com.providences.events.interaction.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.interaction.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {

    Set<MessageEntity> findByChatId(String chatId);

    Set<MessageEntity> findByChatIdAndIsReadFalse(String chatId);
}