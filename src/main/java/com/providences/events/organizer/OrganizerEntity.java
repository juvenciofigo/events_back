package com.providences.events.organizer;

import java.time.LocalDateTime;
import java.util.Set;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.interaction.entities.MessageEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.reviews.ReviewEntity;
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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizers")
@Setter
@Getter
@EqualsAndHashCode(of = { "id" })
@AllArgsConstructor
@NoArgsConstructor
public class OrganizerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, length = 100, name = "company_name")
    private String companyName;

    @Column(length = 15, nullable = false)
    private String phone;

    private String description;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "payerOrganizer", fetch = FetchType.LAZY)
    private Set<PaymentEntity> payments;

    @OneToMany(mappedBy = "receiverOrganizer", fetch = FetchType.LAZY)
    private Set<PaymentEntity> receivers;

    @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY)
    private Set<EventEntity> events;

    // relacionameto com comentarios

    // Relacionemnto com comentarios
    @OneToMany(mappedBy = "senderOrganizer", fetch = FetchType.LAZY)
    private Set<ReviewEntity> senderReviews;

    @OneToMany(mappedBy = "receiverOrganizer", fetch = FetchType.LAZY)
    private Set<ReviewEntity> receiverReviews;

    // relacionameto com participacoes em conversas
    @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY)
    private Set<ParticipantChatEntity> participantChat;

    // Relacionamento como mensagem
    @OneToMany(mappedBy = "senderOrganizer", fetch = FetchType.LAZY)
    private Set<MessageEntity> chats;

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
        return "OrganizerEntity [id=" + id + ", companyName=" + companyName + ", phone=" + phone + ", description=" + description
                + ", profilePicture=" + profilePicture + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

   

}