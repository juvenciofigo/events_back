package com.providences.events.reviews;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<ReviewsEntity, String> {

}