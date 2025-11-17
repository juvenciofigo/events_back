package com.providences.events.supplier;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, String> {

    boolean existsByCompanyName(String name);

    boolean existsByUserId(String userId);

    @Query("""
            SELECT s
            FROM SupplierEntity s
            LEFT JOIN FETCH s.services ser
            LEFT JOIN FETCH s.user u
            LEFT JOIN FETCH u.organizer o
            WHERE s.id =:supplierId
            """)
    Optional<SupplierEntity> findId(@Param("supplierId") String supplierId);
}