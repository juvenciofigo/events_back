package com.providences.events.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {

      List<ServiceEntity> findBySupplier_Id(String id);

}