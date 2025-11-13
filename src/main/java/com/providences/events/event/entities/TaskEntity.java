package com.providences.events.event.entities;

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
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    ////
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "responsible_name")
    private String responsibleName;

    @Column(name = "responsible_phone")
    private String responsiblePhone;

    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(nullable = false,name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

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

    public enum TaskPriority {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        DONE
    }

    @Override
    public String toString() {
        return "TaskEntity [id=" + id + ", responsibleName=" + responsibleName + ", responsiblePhone="
                + responsiblePhone + ", title=" + title + ", description=" + description + ", dueDate=" + dueDate
                + ", priority=" + priority + ", taskStatus=" + taskStatus + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

}