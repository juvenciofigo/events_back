package com.providences.events.event.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.providences.events.event.entities.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {

    @Query("""
                Select obj
                FROM  EventEntity obj
                JOIN FETCH obj.organizer org
                WHERE org.user = :userID
            """)
    List<EventEntity> getEventByUser(@Param("userID") String userID);

    @Query("""
                SELECT obj
                FROM EventEntity obj
                JOIN FETCH obj.organizer org
                JOIN FETCH org.user usr
                LEFT JOIN FETCH usr.supplier supp
                WHERE obj.id = :eventId
            """)
    EventEntity getEventById(@Param("eventId") String eventId);

    // @EntityGraph(attributePaths = {
    // "organizer",
    // "organizer.user",
    // "services",
    // "services.supplier",
    // "suppliers"
    // })
    // @Query("SELECT obj FROM EventEntity obj WHERE obj.id = :eventId")
    // EventEntity getEvent(@Param("eventId") String eventId);
}