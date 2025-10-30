package com.providences.events.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.event.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity,String>  {

    
}