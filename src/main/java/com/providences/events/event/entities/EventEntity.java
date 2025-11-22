package com.providences.events.event.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.location.LocationEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.ticket.entities.TicketEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    private String type;

    // relacionamento com o organizador/criador do evento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "organizer_id")
    private OrganizerEntity organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Column(nullable = false, length = 150)
    private String title;

    private String description;

    @Column(nullable = false, name = "date_start")
    private LocalDateTime dateStart;

    @Column(name = "date_end")
    private LocalDateTime dateEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "event_status")
    private EventStatus eventStatus;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "estimated_guest")
    private Integer estimatedGuest;

    @Column(name = "budget_estimated", precision = 10, scale = 2)
    private BigDecimal budgetEstimated;

    @Column(name = "budget_spent", precision = 10, scale = 2)
    private BigDecimal budgetSpent;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    // relacionamento como ticket/convite/ingreço
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<TicketEntity> tickets;

    // relacionamento como servicos do evento
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<ServicesHasEventEntity> servicesHasEvent;

    // relacionamento como servicos do tarefa
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<TaskEntity> tasks;

    // relacionamento como servicos do despesa
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<ExpenseEntity> expenses;

    // relacionamento como chats
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<ChatEntity> chats;

    // relacionamento como tipos de convites
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<SeatEntity> seats;

    // ///////////
    @PrePersist
    protected void onCreate() {
        eventStatus = EventStatus.PLANNED;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EventStatus {
        PLANNED,
        ONGOING,
        COMPLETED,
        CANCELED
    }

    @Override
    public String toString() {
        return "EventEntity [id=" + id + ", type=" + type + ", isPublic=" + isPublic + ", title=" + title
                + ", description=" + description + ", dateStart=" + dateStart + ", dateEnd=" + dateEnd
                + ", eventStatus=" + eventStatus + ", coverImage=" + coverImage + ", estimatedGuest=" + estimatedGuest
                + ", budgetEstimated=" + budgetEstimated + ", budgetSpent=" + budgetSpent + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + "]";
    }

}