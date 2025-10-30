package com.providences.events.plans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.plans.entities.SupplierPlanEntity;

public interface SupplierPlanRepository extends JpaRepository<SupplierPlanEntity, String> {

}