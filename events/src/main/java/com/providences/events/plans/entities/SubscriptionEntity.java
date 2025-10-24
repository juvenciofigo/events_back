package com.providences.events.plans.entities;

import java.time.LocalDateTime;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.supplier_service.SupplierServicesEntity;

import jakarta.persistence.CascadeType;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    //////

    @Column(nullable = false)
    private LocalDateTime start_date;

    @Column(nullable = false)
    private LocalDateTime end_date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    private Boolean auto_renew = false;
    // Relacionamento como pagamentos
    @OneToOne(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentEntity payment;

    // relacionamentos com potencias planos
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "plan_type")
    private PlanType planType;

    // relacionamento com os planos de fornecedores de serviços
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_plan_id")
    private SupplierPlanEntity supplierPlan;

    // relacionamento como Addons
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_plan_id")
    private AddonPlanEntity addonPlan;

    // relacionamento com os planos de organizadores
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_plan_id")
    private OrganizerPlanEntity organizerPlan;

    // relacionamentos com potencias pagadores
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payer_type")
    private PayerType payerType;

    // `relacionamento como provedor de serviços,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_supplier_id")
    private SupplierServicesEntity supplierServices;

    // ralacionamento com organizador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private OrganizerEntity organizer;

    // ///////////
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
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

    public enum PlanType {
        organizer,
        supplier,
        addon
    }

    public enum PayerType {
        organizer,
        supplier,
    }
}