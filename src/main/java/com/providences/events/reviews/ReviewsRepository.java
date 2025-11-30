package com.providences.events.reviews;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewEntity, String> {

    // Reviews para um serviço
    @Query("""
            SELECT r FROM ReviewEntity r
            WHERE r.target = 'SERVICE'
            AND r.targetService.id = :serviceId
            """)
    Page<ReviewEntity> findByService(@Param("serviceId") String serviceId, Pageable pageable);

    // Reviews para um fornecedor
    @Query("""
            SELECT r FROM ReviewEntity r
            WHERE r.target = 'SUPPLIER'
            AND r.targetSupplier.id = :supplierId
            """)
    Page<ReviewEntity> findBySupplier(@Param("supplierId") String supplierId, Pageable pageable);

    // Reviews para um organizador
    @Query("""
            SELECT r FROM ReviewEntity r
            WHERE r.target = 'ORGANIZER'
            AND r.targetOrganizer.id = :organizerId
            """)
    Page<ReviewEntity> findByOrganizer(@Param("organizerId") String organizerId, Pageable pageable);
}
