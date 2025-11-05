package com.providences.events.supplier;

import java.time.LocalDateTime;
import java.util.List;

import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.location.LocationEntity;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.reviews.ReviewEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.user.UserEntity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suppliers")
@Builder
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "profile_picture")
    private String profilePicture;
    private String logo;

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private LocationEntity location;

    private String description;

    @Column(nullable = false, unique = true, name = "company_name")
    private String companyName;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    // Relecionamento no caso de fazer pagamento
    @OneToMany(mappedBy = "payerSupplier")
    private List<PaymentEntity> payments;

    // Relecionamento no caso de receber pagamento
    @OneToMany(mappedBy = "receiverSupplier")
    private List<PaymentEntity> receivers;

    // Relecionamento com serviços
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceEntity> services;

    // relacionameto com participacoes em conversas
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipantChatEntity> participantChat;

    // Relacionemnto com comentarios
    @OneToMany(mappedBy = "senderSupplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewEntity> senderReviews;

    @OneToMany(mappedBy = "receiverSupplier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewEntity> receiverReviews;

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
