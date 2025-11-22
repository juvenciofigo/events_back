package com.providences.events.plans.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.entities.SubscriptionEntity.PayerType;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {
    Set<SubscriptionEntity> findAllByEndDateAndAutoRenew(LocalDateTime endDate, Boolean autoRenew);

    @Query("""
            SELECT s
            FROM SubscriptionEntity s
            LEFT JOIN FETCH s.payment p
            LEFT JOIN FETCH p.reference ps
            LEFT JOIN FETCH s.organizer o
            LEFT JOIN FETCH s.supplier sup
            LEFT JOIN FETCH o.user oU
            WHERE s.id = :subId
            """)
    Optional<SubscriptionEntity> findId(@Param("subId") String subId);

    @Query("""
            SELECT s
            FROM SubscriptionEntity s
            LEFT JOIN FETCH s.payment p
            LEFT JOIN FETCH p.reference ps
            LEFT JOIN FETCH s.organizer o
            LEFT JOIN FETCH s.supplier sup
            LEFT JOIN FETCH o.user oU
            WHERE s.organizer.id = :organizerId AND s.payerType = :payerType
            """)
    Set<SubscriptionEntity> findByPayerTypeAndOrganizer(@Param("organizerId") String organizerId,
            @Param("payerType") PayerType payerType);

    @Query("""
            SELECT s
            FROM SubscriptionEntity s
            LEFT JOIN FETCH s.payment p
            LEFT JOIN FETCH p.reference ps
            LEFT JOIN FETCH s.organizer o
            LEFT JOIN FETCH s.supplier sup
            LEFT JOIN FETCH sup.user sU
            WHERE s.supplier.id = :supplierId AND s.payerType = :payerType
            """)
    Set<SubscriptionEntity> findByPayerTypeAndSupplier(@Param("supplierId") String supplierId,
            @Param("payerType") PayerType payerType);
}