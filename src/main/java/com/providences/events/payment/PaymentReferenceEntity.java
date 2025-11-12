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

@Entity
@Table(name = "payment_reference")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(unique = true, name = "reference_code")
    private String thirdPartyReference;

    @Column(unique = true, name = "transaction_reference")
    private String transactionReference;

    @Column(columnDefinition = "json")
    private String gatewayResponse;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "PaymentReferenceEntity [id=" + id + ", thirdPartyReference=" + thirdPartyReference
                + ", transactionReference=" + transactionReference + ", gatewayResponse=" + gatewayResponse
                + ", createdAt=" + createdAt + "]";
    }

}