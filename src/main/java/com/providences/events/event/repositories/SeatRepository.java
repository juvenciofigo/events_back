package com.providences.events.event.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.event.entities.SeatEntity;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, String> {
    @Query("""
            SELECT s
            FROM SeatEntity s
            LEFT JOIN FETCH s.event e
            LEFT JOIN FETCH s.tickets t
            LEFT JOIN FETCH e.organizer o
            LEFT JOIN FETCH o.user u
            LEFT JOIN FETCH u.supplier sp
            WHERE s.id = :seatId
            """)
    Optional<SeatEntity> getSeat(@Param("seatId") String seatId);
}