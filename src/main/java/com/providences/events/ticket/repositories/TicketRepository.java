package com.providences.events.ticket.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.providences.events.ticket.entities.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, String> {

    Optional<TicketEntity> findByEventId(String event);

    @Query("""
                SELECT t
                FROM TicketEntity t
                LEFT JOIN FETCH  t.guest
                LEFT JOIN FETCH  t.seat
                WHERE t.id = :ticketId
            """)
    Optional<TicketEntity> getTicketById(@Param("ticketId") String ticketId);

    @Query("""
                SELECT t
                FROM TicketEntity t
                LEFT JOIN FETCH  t.guest
                LEFT JOIN FETCH  t.seat
                WHERE t.accessToken = :token
            """)
    Optional<TicketEntity> findByAccessToken(String token);

    Optional<TicketEntity> findByTicketCode(String code);

}