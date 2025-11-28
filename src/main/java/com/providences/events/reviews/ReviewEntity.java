package com.providences.events.reviews;

import java.time.LocalDateTime;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.services.ServiceEntity;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    // //////////////
    @Column(nullable = false, columnDefinition = "Text")
    private String comment;

    private Double rating;

    // Sender
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewSender sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_supplier_id")
    private SupplierEntity senderSupplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_organizer_id")
    private OrganizerEntity senderOrganizer;

    // Target
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewTarget target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_supplier_id")
    private SupplierEntity targetSupplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_organizer_id")
    private OrganizerEntity targetOrganizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_service_id")
    private ServiceEntity targetService;

    // ///////////
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ReviewTarget {
        SERVICE,
        SUPPLIER,
        ORGANIZER
    }

    public enum ReviewSender {
        SUPPLIER,
        ORGANIZER
    }

    @Override
    public String toString() {
        return "ReviewEntity [id=" + id + ", comment=" + comment + ", rating=" + rating + ", sender=" + sender
                + ", target=" + target + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}