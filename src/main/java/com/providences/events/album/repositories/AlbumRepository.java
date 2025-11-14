package com.providences.events.album.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.providences.events.album.entities.AlbumEntity;

public interface AlbumRepository extends JpaRepository<AlbumEntity, String> {

    @Query("""
                Select a
                FROM  AlbumEntity a
                LEFT JOIN FETCH  a.medias
                WHERE a.id = :albumId
            """)
    Optional<AlbumEntity> findMediasByAlbumId(@Param("albumId") String albumId);

    @Query("""
                Select a
                FROM  AlbumEntity a
                LEFT JOIN FETCH  a.medias m
                LEFT JOIN FETCH  a.service s
                LEFT JOIN FETCH  s.supplier su
                LEFT JOIN FETCH  su.user u
                LEFT JOIN FETCH  u.organizer o
                WHERE a.id = :albumId
            """)
    Optional<AlbumEntity> findId(@Param("albumId") String albumId);
}