package com.providences.events.payment.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.providences.events.payment.PaymentReferenceEntity;

@Repository
public interface PaymentReferenceRepository extends JpaRepository<PaymentReferenceEntity, String> {

}
