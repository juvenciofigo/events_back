package com.providences.events.plans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.plans.entities.AddonPlanEntity;

public interface AddonPlanRepository extends JpaRepository<AddonPlanEntity, String> {

}