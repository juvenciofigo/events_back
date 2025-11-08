package com.providences.events.interaction.entities;

import java.time.LocalDateTime;
import com.providences.events.guest.GuestEntity;
import com.providences.events.organizer.OrganizerEntity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participant_chat")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    ////
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantType type;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    // relacionameto com organizador
    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private OrganizerEntity organizer;

    // relacionameto com fornecedor de servicos
    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private SupplierEntity supplier;

    // relacionameto com fornecedor de servicos
    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id")
    private GuestEntity guest;

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

    public enum ParticipantType {
        SUPPLIER,
        GUEST,
        ORGANIZER,
        ADMIN
    }

    @Override
    public String toString() {
        return "ParticipantChatEntity [id=" + id + ", type=" + type + ", chat=" + chat + ", organizer=" + organizer
                + ", supplier=" + supplier + ", guest=" + guest + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

}