package com.providences.events.event.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    ////
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    private String title;
    private String category;
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private ExpensePaymentStatus paymentStatus;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

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

    enum ExpensePriority {
        LOW,
        MEDIUM,
        HIGH
    }

    enum ExpenseStatus {
        PENDING,
        IN_PROGRESS,
        DONE
    }

    enum ExpensePaymentStatus {
        PAID,
        PENDING
    }

    @Override
    public String toString() {
        return "ExpenseEntity [id=" + id + ", title=" + title + ", category=" + category + ", description="
                + description + ", amount=" + amount + ", paymentStatus=" + paymentStatus + ", dueDate=" + dueDate
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

  

}