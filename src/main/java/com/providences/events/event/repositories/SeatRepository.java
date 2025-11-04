package com.providences.events.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.providences.events.event.entities.SeatsEntity;

@Repository
public interface SeatRepository extends JpaRepository<SeatsEntity, String> {
}