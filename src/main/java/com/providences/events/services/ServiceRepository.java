package com.providences.events.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {

      // Set<ServiceEntity> findBySupplier_Id(String id);

      @Query("""
                  SELECT s
                  FROM ServiceEntity s
                  LEFT JOIN FETCH s.supplier sup
                  WHERE sup.id=:id
                  """)
      Set<ServiceEntity> findBySupplier_Id(@Param("id") String id);

      @Query("""
                  SELECT ser
                  FROM ServiceEntity ser
                  LEFT JOIN FETCH ser.supplier sup
                  LEFT JOIN FETCH ser.albums a
                  LEFT JOIN FETCH sup.user usr
                  LEFT JOIN FETCH  usr.organizer org
                  WHERE ser.id = :serviceID
                  """)
      Optional<ServiceEntity> getService(@Param("serviceID") String serviceID);

}