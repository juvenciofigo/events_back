package com.providences.events.reviews;

import java.time.LocalDateTime;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    // //////////////
    @Column(nullable = false, columnDefinition = "Text")
    private String comment;

    private Double rating;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private SupplierEntity supplier;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private OrganizerEntity organizer;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

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