package com.providences.events.album.entities;

import java.time.LocalDateTime;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.supplier.SupplierEntity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medias_album")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false, name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "media_type")
    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private AlbumEntity serviceAlbum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private OrganizerEntity organizer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    // ///////////
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum MediaType {
        IMAGE,
        VIDEO
    }

    @Override
    public String toString() {
        return "MediaEntity [id=" + id + ", fileUrl=" + fileUrl + ", mediaType=" + mediaType + ", createdAt="
                + createdAt + "]";
    }

}