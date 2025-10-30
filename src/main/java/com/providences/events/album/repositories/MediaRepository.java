package com.providences.events.album.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providences.events.album.entities.MediaEntity;

public interface MediaRepository extends JpaRepository<MediaEntity, String> {

}