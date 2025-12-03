package com.providences.events.event.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.event.entities.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {

    @Query("""
                Select e
                FROM  EventEntity e
                LEFT JOIN FETCH e.organizer o
                WHERE o.user = :userID
            """)
    Set<EventEntity> getEventByUser(@Param("userID") String userID);

    @Query("""
                SELECT e
                FROM EventEntity e
                LEFT JOIN FETCH e.organizer o
                LEFT JOIN FETCH e.tasks ta
                LEFT JOIN FETCH o.user u
                LEFT JOIN FETCH e.chats c
                LEFT JOIN FETCH e.tickets t
                LEFT JOIN FETCH t.guest g
                LEFT JOIN FETCH c.participants
                LEFT JOIN FETCH  u.supplier s
                WHERE e.id = :eventId
            """)
    Optional<EventEntity> findId(@Param("eventId") String eventId);

    @Query("""
                SELECT e
                FROM EventEntity e
                LEFT JOIN FETCH e.tickets t
                LEFT JOIN FETCH t.guest g
                LEFT JOIN FETCH e.organizer o
                WHERE e.id = :eventId
            """)
    Optional<EventEntity> findIdFetchTicketsAndGuests(@Param("eventId") String eventId);

    @Query("""
                SELECT e
                FROM EventEntity e
                LEFT JOIN FETCH e.organizer o
                LEFT JOIN FETCH e.seats s
                LEFT JOIN FETCH e.chats c
                WHERE e.id = :eventId
            """)
    Optional<EventEntity> createGuest(@Param("eventId") String eventId);

    @Query("""
                SELECT e
                FROM EventEntity e
                WHERE e.isPublic = true
            """)
    Set<EventEntity> findAllPublicEvents();

    @Query("""
                SELECT e
                FROM EventEntity e
                LEFT JOIN FETCH e.organizer o
                WHERE o.id = :organizerId
            """)
    Page<EventEntity> findByOrganizer(@Param("organizerId") String organizerId, Pageable pageable);
}