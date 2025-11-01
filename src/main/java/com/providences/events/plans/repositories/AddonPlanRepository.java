package com.providences.events.plans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.providences.events.plans.entities.AddonPlanEntity;

@Repository
public interface AddonPlanRepository extends JpaRepository<AddonPlanEntity, String> {

}