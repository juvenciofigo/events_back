package com.providences.events.ticket.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.ticket.entities.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, String> {

    Optional<TicketEntity> findByEventId(String event);
}