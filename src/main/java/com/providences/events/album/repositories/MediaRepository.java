package com.providences.events.album.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.providences.events.album.entities.MediaEntity;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, String> {

}