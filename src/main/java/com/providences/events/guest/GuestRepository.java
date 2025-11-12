package com.providences.events.guest;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestRepository extends JpaRepository<GuestEntity, String> {

    @Query("""
                Select g
                FROM  GuestEntity g
                LEFT JOIN FETCH g.ticket tic
                WHERE tic.event.id = :eventId
            """)

    Set<GuestEntity> findGuestEvent(@Param("eventId") String eventId);

    @Query("""
                Select g
                FROM  GuestEntity g
                LEFT JOIN FETCH g.ticket t
                LEFT JOIN FETCH t.event e
                LEFT JOIN FETCH e.organizer o
                LEFT JOIN FETCH o.user u
                LEFT JOIN FETCH t.seat s
                LEFT JOIN FETCH g.participantChat p
                LEFT JOIN FETCH g.messages m
                WHERE g.id = :guestId
            """)

    Optional<GuestEntity> deleteGuestById(@Param("guestId") String guestId);
}