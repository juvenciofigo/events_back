package com.providences.events.interaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.interaction.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {

}