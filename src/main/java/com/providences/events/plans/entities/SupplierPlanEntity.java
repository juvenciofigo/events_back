package com.providences.events.plans.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "supplier_plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    //////
    @Column(length = 30)
    private String name;

    @Column(length = 45)
    private String description;

    @Column(length = 100)
    private String resources;

    @Column(name = "price_monthly")
    private Double priceMonthly;

    @Column(name = "price_yearly")
    private Double priceYearly;

    @Column(columnDefinition = "json")
    private String features;

    private Integer level;

    @OneToMany(cascade = CascadeType.ALL,  mappedBy = "supplierPlan")
    private Set<SubscriptionEntity> subscriptions;
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

}