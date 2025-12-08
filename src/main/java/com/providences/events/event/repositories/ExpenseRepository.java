package com.providences.events.event.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.event.entities.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, String> {

        @Query("""
                        SELECT e
                        FROM ExpenseEntity e
                        WHERE e.event.id = :eventId
                        """)
        Set<ExpenseEntity> findByEventId(@Param("eventId") String eventId);

        @Query("""
                        SELECT e
                        FROM ExpenseEntity e
                        WHERE e.event.id = :eventId
                        """)
        Page<ExpenseEntity> findByEventIdPageable(@Param("eventId") String eventId, Pageable pageable);

        @Query("""
                        SELECT e
                        FROM ExpenseEntity e
                        JOIN e.event ev
                        JOIN ev.organizer org
                        JOIN org.user u
                        WHERE e.id = :expenseId
                        """)
        Optional<ExpenseEntity> findId(@Param("expenseId") String expenseId);
}
