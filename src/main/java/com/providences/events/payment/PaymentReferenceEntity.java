package com.providences.events.payment;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payment_reference")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ToString.Include
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(unique = true, name = "reference_code")
    @ToString.Include
    private String thirdPartyReference;

    @ToString.Include
    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(columnDefinition = "json")
    @ToString.Include
    private String gatewayResponse;

    @ToString.Include
    private LocalDateTime createdAt = LocalDateTime.now();

}