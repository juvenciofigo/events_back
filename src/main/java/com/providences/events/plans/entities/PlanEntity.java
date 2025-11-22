package com.providences.events.plans.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    //////
    @Column(length = 30, unique = true)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "plan_type")
    private PlanType planType;

    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
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

    public enum PlanType {
        ORGANIZER,
        SUPPLIER,
        ADDON
    }

    @Override
    public String toString() {
        return "PlanEntity [id=" + id + ", name=" + name + ", description=" + description + ", resources=" + resources
                + ", priceMonthly=" + priceMonthly + ", priceYearly=" + priceYearly + ", features=" + features
                + ", level=" + level + ", planType=" + planType + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

    

}