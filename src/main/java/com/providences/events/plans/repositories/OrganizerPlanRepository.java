package com.providences.events.plans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.providences.events.plans.entities.OrganizerPlanEntity;

@Repository
public interface OrganizerPlanRepository extends JpaRepository<OrganizerPlanEntity, String> {
}