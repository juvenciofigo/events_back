package com.providences.events.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.event.entities.ServicesHasEventEntity;

public interface ServicesHasEventRepository extends JpaRepository<ServicesHasEventEntity, String> {

}
