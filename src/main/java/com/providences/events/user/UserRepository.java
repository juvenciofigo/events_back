package com.providences.events.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    

    Optional<UserDetails> findByEmail(String login);

    @Query("""
            SELECT u
            FROM UserEntity u
            LEFT JOIN FETCH u.organizer o
            LEFT JOIN FETCH u.supplier s
            WHERE u.id=:userId
            """)
    Optional<UserEntity> findId(@Param("userId") String userId);

}
