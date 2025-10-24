package com.providences.events.interaction.entities;

import java.time.LocalDateTime;
import com.providences.events.guest.GuestEntity;
import com.providences.events.supplier_service.SupplierServicesEntity;

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

@Entity
@Table(name = "participant_chat")
@Data
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
    /*
     * @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval =
     * true, fetch = FetchType.LAZY)
     * private List<ParticipantChatEntity> participantChat;
     */

    // relacionameto com fornecedor de servicos
    @ManyToOne
    @JoinColumn(name = "supplier_services_id",referencedColumnName = "id")
    private SupplierServicesEntity supplierServices;

    // relacionameto com fornecedor de servicos
    @ManyToOne
    @JoinColumn(name = "guest_id",referencedColumnName = "id")
    private GuestEntity guests;

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
        GUESTS,
        // ORGANIZER,
        // ADMIN
    }

}