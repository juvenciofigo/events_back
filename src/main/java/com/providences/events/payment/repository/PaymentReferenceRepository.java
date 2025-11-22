package com.providences.events.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.providences.events.payment.entities.PaymentReferenceEntity;

@Repository
public interface PaymentReferenceRepository extends JpaRepository<PaymentReferenceEntity, String> {

}
