package com.providences.events.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {

      // List<ServiceEntity> findBySupplier_Id(String id);

      @Query("""
                  SELECT s
                  FROM ServiceEntity s
                  JOIN FETCH s.supplier sup
                  WHERE sup.id=:id
                  """)
      List<ServiceEntity> findBySupplier_Id(@Param("id") String id);

}