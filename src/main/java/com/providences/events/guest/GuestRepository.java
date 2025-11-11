package com.providences.events.guest;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestRepository extends JpaRepository<GuestEntity, String> {

    @Query("""
                Select obj
                FROM  GuestEntity obj
                LEFT JOIN FETCH obj.ticket tic
                WHERE tic.event.id = :eventId
            """)

    Set<GuestEntity> findGuestEvent(@Param("eventId") String eventId);
}