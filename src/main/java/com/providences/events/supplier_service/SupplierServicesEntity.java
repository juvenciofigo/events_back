package com.providences.events.supplier_service;

import java.time.LocalDateTime;
import java.util.List;

import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.location.LocationEntity;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier_reviews.supplier_reviewsEntity;
import com.providences.events.user.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suppliers_services")
public class SupplierServicesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String profile_picture;
    private String logo;

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private LocationEntity location;

    private String description;

    @Column(nullable = false, unique = true)
    private String company_name;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relecionamento no caso de fazer pagamento
    @OneToMany(mappedBy = "payer_supplier")
    private List<PaymentEntity> payments;
    
    // Relecionamento no caso de receber pagamento
    @OneToMany(mappedBy = "receiver_supplier")
    private List<PaymentEntity> receivers;
    
    // Relecionamento com serviços
    @OneToMany(mappedBy = "serviceSupplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceEntity> services;

    // Relacionamento com comentarios
    @OneToMany(mappedBy = "supplierService", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<supplier_reviewsEntity> reviews;

    // relacionameto com participacoes em conversas
    @OneToMany(mappedBy = "supplierServices", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipantChatEntity> participantChat;

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
