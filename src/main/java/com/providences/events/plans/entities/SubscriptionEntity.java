package com.providences.events.plans.entities;

import java.time.LocalDateTime;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.plans.entities.PlanEntity.PlanType;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    //////

    @Column(nullable = false, name = "start_date")
    private LocalDateTime startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false, name = "auto_renew")
    private Boolean autoRenew = false;

    // Relacionamento como pagamentos
    @OneToOne(mappedBy = "targetSubscription", fetch = FetchType.LAZY)
    private PaymentEntity payment;

    // relacionamentos com potencias planos
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "plan_type")
    private PlanType planType;

    // relacionamento com os planos de organizadores
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private PlanEntity plan;

    // relacionamentos com potencias pagadores
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payer_type")
    private PayerType payerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle")
    private BillingCycle billingCycle;

    // `relacionamento como provedor de serviços,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;

    // relacionamento com organizador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private OrganizerEntity organizer;

    // ///////////
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = SubscriptionStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum SubscriptionStatus {
        ACTIVE,
        CANCELLED,
        EXPIRED
    }

    public enum PlanSubscripted {
        ORGANIZER,
        SUPPLIER,
        ADDON
    }

    public enum PayerType {
        ORGANIZER,
        SUPPLIER,
    }

    public enum BillingCycle {
        MONTHLY,
        YEARLY
    }

    @Override
    public String toString() {
        return "SubscriptionEntity [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", status="
                + status + ", autoRenew=" + autoRenew + ", planType=" + planType + ", payerType=" + payerType
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}