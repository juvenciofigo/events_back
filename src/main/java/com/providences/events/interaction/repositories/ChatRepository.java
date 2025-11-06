package com.providences.events.interaction.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ChatEntity.ChatType;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, String> {

        @Query("""
                            Select chat
                            FROM  ChatEntity chat
                            WHERE chat.event.id = :eventId AND chat.type=:type
                        """)
        Optional<ChatEntity> findByEventAndType(@Param("eventId") String eventId, @Param("type") ChatType type);

        @Query("""
                            SELECT chat
                            FROM ChatEntity chat
                            JOIN chat.participants p
                            WHERE chat.event.id = :eventId
                              AND chat.type = :type
                              AND p.organizer.id = :participantId
                        """)
        Optional<ChatEntity> findByEventAndTypeAndParticipantOrganizer(
                        @Param("eventId") String eventId,
                        @Param("type") ChatType type,
                        @Param("participantId") String participantId);

        @Query("""
                            SELECT chat
                            FROM ChatEntity chat
                            JOIN chat.participants p
                            WHERE chat.event.id = :eventId
                              AND chat.type = :type
                              AND p.organizer.id = :participantId
                        """)
        Optional<ChatEntity> findByEventAndTypeAndParticipantSupplier(
                        @Param("eventId") String eventId,
                        @Param("type") ChatType type,
                        @Param("participantId") String participantId);

}
