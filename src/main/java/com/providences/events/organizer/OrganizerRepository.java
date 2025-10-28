package com.providences.events.organizer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, String> {
    Optional<OrganizerEntity> findByUserId(String UserId);
}
