package com.providences.events.album.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.album.entities.AlbumEntity;

public interface AlbumRepository extends JpaRepository<AlbumEntity, String> {

}