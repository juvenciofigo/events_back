package com.providences.events.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.providences.events.album.entities.AlbumEntity;
import com.providences.events.event.entities.ServicesHasEventEntity;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.reviews.ReviewEntity;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // relacionamento com o fornecedor desse serviço
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private SupplierEntity supplier;

    @JoinColumn(nullable = false)
    private String category;

    private String description;

    @Column(name = "price_base", precision = 10, scale = 2)
    private BigDecimal priceBase;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<ServiceUnavailability> unavailability;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // relaciomanto com pagamentos
    @OneToMany(mappedBy = "targetService", fetch = FetchType.LAZY)
    private Set<PaymentEntity> payments;

    // relacionamento com algum de midia
    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<AlbumEntity> albums;

    // relacionamento como servicos do evento
    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<ServicesHasEventEntity> servicesHasEvent;

    // relacionameto com comentarios
    @OneToMany(mappedBy = "targetService", fetch = FetchType.LAZY)
    private Set<ReviewEntity> receiverReviews;

    // ///////////
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ServiceEntity [id=" + id + ", category=" + category + ", description=" + description + ", priceBase="
                + priceBase + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
