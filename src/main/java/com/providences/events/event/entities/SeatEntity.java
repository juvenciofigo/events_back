package com.providences.events.event.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.providences.events.payment.PaymentEntity;
import com.providences.events.ticket.entities.TicketEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(nullable = false, name = "is_paid")
    private Boolean isPaid;

    @PositiveOrZero
    @Column(precision = 10, scale = 2, name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "layout_position_x")
    private Double layoutPositionX;

    @Column(name = "layout_position_y")
    private Double layoutPositionY;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "seat", fetch = FetchType.LAZY)
    private Set<TicketEntity> tickets;

    @OneToMany(mappedBy = "seat", fetch = FetchType.LAZY)
    private Set<PaymentEntity> payment;

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
        return "SeatEntity [id=" + id + ", name=" + name + ", description=" + description + ", totalSeats=" + totalSeats
                + ", availableSeats=" + availableSeats + ", layoutPositionX=" + layoutPositionX + ", layoutPositionY="
                + layoutPositionY + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", tickets=" + tickets
                + "]";
    }

}
