package com.providences.events.event.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.event.entities.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, String> {

    @Query("""
            SELECT t
            FROM TaskEntity t
            LEFT JOIN FETCH t.event e
            LEFT JOIN FETCH e.organizer o
            LEFT JOIN FETCH o.user u
            LEFT JOIN FETCH u.supplier s
            WHERE t.id = :taskId
            """)
    Optional<TaskEntity> getTask(@Param("taskId") String taskId);
}