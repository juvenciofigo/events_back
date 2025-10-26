package com.providences.events.interaction.entities;

import java.time.LocalDateTime;
import java.util.List;


import com.providences.events.event.entities.EventEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    ////
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TYPE type;

    // relacionamento como mensagem
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages;

    // relacionamento como participante
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantChatEntity> participants;

    // relacionamento como evento
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false, referencedColumnName = "id")
    private EventEntity event;



    @Column(nullable = false)
    private String title;
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

    public enum TYPE {
        SUPPLIER,
        GUESTS,
        ORGANIZER,
        ADMIN
    }

}