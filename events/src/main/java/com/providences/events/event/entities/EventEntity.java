package com.providences.events.event.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.providences.events.event.enums.EventStatus;
import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.location.LocationEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.ticket.TicketEntity;

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
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    private String type;

    // relacionamento com o organizador/criador do evento
    @ManyToOne
    @JoinColumn(nullable = false, name = "organizer_id")
    private OrganizerEntity organizer;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Column(nullable = false, length = 150)
    private String name;

    private String description;

    @Column(nullable = false, name = "date_start")
    private LocalDateTime dateStart;

    @Column(nullable = false, name = "date_end")
    private LocalDateTime dateEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "event_status")
    private EventStatus eventStatus;

    private String cover_image;

    @Column(name = "estimated_guest")
    private Integer estimatedGuest;

    @Column(name = "budget_estimated", precision = 10, scale = 2)
    private BigDecimal budgetEstimated;

    @Column(name = "budget_spent", precision = 10, scale = 2)
    private BigDecimal budgetSpent;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // relacionamento como ticket/convite/ingreço
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketEntity> tickets;

    // relacionamento como servicos do evento
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicesHasEventEntity> servicesHasEvent;

    // relacionamento como servicos do tarefa
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> tasks;

    // relacionamento como servicos do despesa
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses;

    // relacionamento como chats
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatEntity> chats;

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