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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    ////
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, name="is_read")
    private Boolean isRead = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType sender;

    @ManyToOne
    @JoinColumn(name = "sender_supplier_id")
    private SupplierEntity senderSupplier;

    @ManyToOne
    @JoinColumn(name = "sender_organizer_id")
    private OrganizerEntity senderOrganizer;

    @ManyToOne
    @JoinColumn(name = "sender_guest_id")
    private GuestEntity senderGuest;

    @Column(name = "is_admin", nullable = false)
    private boolean senderAdmin = false;

    // ///////////
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum SenderType {
        SUPPLIER,
        GUEST,
        ORGANIZER,
        ADMIN
    }

    @Override
    public String toString() {
        return "MessageEntity [id=" + id + ", chat=" + chat + ", content=" + content + ", isRead=" + isRead
                + ", sender=" + sender + ", senderSupplier=" + senderSupplier + ", senderOrganizer=" + senderOrganizer
                + ", senderGuest=" + senderGuest + ", senderAdmin=" + senderAdmin + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + "]";
    }

}