package com.providences.events.ticket;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, String> {

    Optional<TicketEntity> findByEventId(String event);
}