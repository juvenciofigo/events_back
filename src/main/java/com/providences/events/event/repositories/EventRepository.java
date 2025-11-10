package com.providences.events.event.repositories;

import java.util.Optional;
import java.util.Set;

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
                JOIN FETCH e.organizer o
                WHERE o.user = :userID
            """)
    Set<EventEntity> getEventByUser(@Param("userID") String userID);

    @Query("""
                SELECT e
                FROM EventEntity e
                JOIN FETCH e.organizer o
                JOIN FETCH o.user u
                JOIN FETCH e.chats c
                JOIN FETCH e.tickets t
                JOIN FETCH c.participants
                LEFT JOIN FETCH u.supplier s
                WHERE e.id = :eventId
            """)
    Optional<EventEntity> findId(@Param("eventId") String eventId);

    @Query("""
                SELECT e
                FROM EventEntity e
                JOIN FETCH e.tickets t
                JOIN FETCH t.guest g
                WHERE e.id = :eventId
            """)
    Optional<EventEntity> findIdFetchTickesAndGuests(@Param("eventId") String eventId);

}