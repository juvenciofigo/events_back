package com.providences.events.supplier;

import java.time.LocalDateTime;
import java.util.Set;

import com.providences.events.interaction.entities.MessageEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.location.LocationEntity;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.reviews.ReviewEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.user.UserEntity;

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
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suppliers")
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "profile_picture")
    private String profilePicture;
    
    private String logo;

    @OneToOne(fetch = FetchType.LAZY)
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
    @OneToMany(mappedBy = "payerSupplier", fetch = FetchType.LAZY)
    private Set<PaymentEntity> payments;

    // Relecionamento no caso de receber pagamento
    @OneToMany(mappedBy = "receiverSupplier", fetch = FetchType.LAZY)
    private Set<PaymentEntity> receivers;

    // Relecionamento com serviços
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<ServiceEntity> services;

    // relacionameto com participacoes em conversas
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<ParticipantChatEntity> participantChat;

    // Relacionamento como mensagem
    @OneToMany(mappedBy = "senderSupplier", fetch = FetchType.LAZY)
    private Set<MessageEntity> chats;

    // Relacionemnto com comentarios
    @OneToMany(mappedBy = "senderSupplier", fetch = FetchType.LAZY)
    private Set<ReviewEntity> senderReviews;

    @OneToMany(mappedBy = "receiverSupplier", fetch = FetchType.LAZY)
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
        return "SupplierEntity [id=" + id + ", profilePicture=" + profilePicture + ", logo=" + logo + ", description="
                + description + ", companyName=" + companyName + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + "]";
    }

}
