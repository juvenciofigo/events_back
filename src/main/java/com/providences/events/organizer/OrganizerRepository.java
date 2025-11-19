package com.providences.events.organizer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, String> {
    Optional<OrganizerEntity> findByUserId(String userId);

       boolean existsByUserId(String userId);

    @Query("""
            SELECT  o
            FROM OrganizerEntity o
            LEFT JOIN FETCH o.user u
            LEFT JOIN FETCH u.supplier s
            LEFT JOIN FETCH o.events e
            WHERE o.id = :organizerId
                    """)
    Optional<OrganizerEntity> findId(@Param("organizerId") String organizerId);

}
