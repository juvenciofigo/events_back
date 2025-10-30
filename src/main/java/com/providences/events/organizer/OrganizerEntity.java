package com.providences.events.organizer;

import java.time.LocalDateTime;
import java.util.List;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.reviews.ReviewsEntity;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "organizers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 100)
    private String name;


    @Column(length = 15, nullable = false)
    private String phone;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "payer_organizer")
    private List<PaymentEntity> payments;

    @OneToMany(mappedBy = "receiver_organizer")
    private List<PaymentEntity> receivers;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events;

    // relacionameto com comentarios
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewsEntity> reviews;

    // relacionameto com participacoes em conversas
    // @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval =
    // true, fetch = FetchType.LAZY)
    // private List<ParticipantChatEntity> participantChat;

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