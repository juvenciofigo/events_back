package com.providences.events.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.providences.events.event.entities.ServicesHasEventEntity;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.service_album.Albums_serviceEntity;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier_reviews.supplier_reviewsEntity;

import jakarta.persistence.CascadeType;
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
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "services")
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // relacionamento com o fornecedor desse serviço
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = false)
    private SupplierEntity supplier;

    @JoinColumn(nullable = false)
    private String category;

    private String description;

    @Column(name = "price_base", precision = 10, scale = 2)
    private BigDecimal priceBase;

    @OneToMany(mappedBy = "service")
    private List<ServiceUnavailability> unavailability;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // relaciomanto com pagamentos
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments;

    // relacionamento com algum de midia
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Albums_serviceEntity> albums;

    // relacionamento como servicos do evento
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicesHasEventEntity> servicesHasEvent;

    // relacionameto com comentarios
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<supplier_reviewsEntity> reviews;

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

}