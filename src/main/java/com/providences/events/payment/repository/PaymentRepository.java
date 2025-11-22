package com.providences.events.payment.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.payment.entities.PaymentEntity.PayerType;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
        @Query("""
                        SELECT p
                        FROM PaymentEntity p
                        LEFT JOIN FETCH p.payerOrganizer pO
                        WHERE pO.id = :payerId AND p.payerType = :payerType
                        """)
        Set<PaymentEntity> findByPayerTypeAndOrganizer(@Param("payerId") String payerId,
                        @Param("payerType") PayerType payerType);

        @Query("""
                        SELECT p
                        FROM PaymentEntity p
                        LEFT JOIN FETCH p.payerSupplier pS
                        WHERE pS.id = :payerId AND p.payerType = :payerType
                        """)
        Set<PaymentEntity> findByPayerTypeAndSupplier(@Param("payerId") String payerId,
                        @Param("payerType") PayerType payerType);

        @Query("""
                        SELECT p
                        FROM PaymentEntity p
                        LEFT JOIN FETCH p.payerGuest pG
                        WHERE pG.id = :payerId AND p.payerType = :payerType
                        """)
        Set<PaymentEntity> findByPayerTypeAndGuest(@Param("payerId") String payerId,
                        @Param("payerType") PayerType payerType);

        @Query("""
                        SELECT p
                        FROM PaymentEntity p
                        LEFT JOIN FETCH p.payerSupplier pS
                        LEFT JOIN FETCH p.payerOrganizer pO
                        LEFT JOIN FETCH p.payerGuest pG
                        LEFT JOIN FETCH p.receiverSupplier pRS
                        LEFT JOIN FETCH p.receiverOrganizer pRO
                        LEFT JOIN FETCH p.receiverPlatform pRP
                        LEFT JOIN FETCH p.targetService pTS
                        LEFT JOIN FETCH p.targetSubscription pTSub
                        LEFT JOIN FETCH p.targetSeat pTSeat
                        WHERE p.id = :paymentId
                        """)
        Optional<PaymentEntity> getId(@Param("paymentId") String paymentId);

}
