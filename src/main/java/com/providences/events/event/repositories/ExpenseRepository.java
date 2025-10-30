package com.providences.events.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.event.entities.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, String> {

}
