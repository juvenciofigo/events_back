package com.providences.events.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.event.entities.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, String> {

}