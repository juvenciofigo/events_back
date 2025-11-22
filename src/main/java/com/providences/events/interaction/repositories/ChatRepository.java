package com.providences.events.interaction.repositories;

import java.util.List;
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
                LEFT JOIN FETCH  chat.participants
                LEFT JOIN FETCH  chat.messages
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

    @Query("""
                SELECT chat
                FROM ChatEntity chat
                LEFT JOIN FETCH chat.participants p
                LEFT JOIN FETCH p.guest
                LEFT JOIN FETCH chat.messages m
                LEFT JOIN FETCH chat.event e
                LEFT JOIN FETCH e.organizer o
                LEFT JOIN FETCH o.user u
                LEFT JOIN FETCH u.supplier
                WHERE chat.id = :chat
            """)
    Optional<ChatEntity> findByIdAndParticipants(String chat);

    @Query("""
            SELECT DISTINCT c
            FROM ChatEntity c
            LEFT JOIN FETCH  c.participants p
            WHERE c.type = ChatType.SUPPLIER
            AND p.supplier.id = :supplierId
            """)
    List<ChatEntity> findSupplierChats(String supplierId);

    @Query("""
            SELECT DISTINCT
                c FROM ChatEntity c
                LEFT JOIN FETCH c.participants p
                WHERE c.event.id = :eventId
            """)
    List<ChatEntity> findOrganizerEventChats(@Param("eventId") String eventId);

    @Query("""
                SELECT DISTINCT c FROM ChatEntity c
                LEFT JOIN FETCH c.participants p
                WHERE c.type = 'GUEST_CHAT'
                AND c.event.id = :eventId
                AND p.guest.id = :guestId
            """)
    Optional<ChatEntity> findGuestChat(@Param("eventId") String eventId, @Param("guestId") String guestId);

}
