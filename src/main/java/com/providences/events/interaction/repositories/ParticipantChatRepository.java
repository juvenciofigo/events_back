package com.providences.events.interaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.providences.events.interaction.entities.ParticipantChatEntity;

public interface ParticipantChatRepository extends JpaRepository<ParticipantChatEntity, String> {

}
