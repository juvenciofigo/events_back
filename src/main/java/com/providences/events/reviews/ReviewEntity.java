package com.providences.events.reviews;

import java.time.LocalDateTime;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @ManyToOne
    @JoinColumn(name = "sender_supplier_id")
    private SupplierEntity senderSupplier;

    @ManyToOne
    @JoinColumn(name = "sender_organizer_id")
    private OrganizerEntity senderOrganizer;

    // Target
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewTarget target;

    @ManyToOne
    @JoinColumn(name = "receiver_supplier_id")
    private SupplierEntity receiverSupplier;

    @ManyToOne
    @JoinColumn(name = "receiver_organizer_id")
    private OrganizerEntity receiverOrganizer;

    @ManyToOne
    @JoinColumn(name = "receiver_service_id")
    private ServiceEntity receiverService;

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

   public enum ReviewTarget {
        SERVICE,
        SUPPLIER,
        ORGANIZER
    }

   public enum ReviewSender {
        SUPPLIER,
        ORGANIZER
    }
}