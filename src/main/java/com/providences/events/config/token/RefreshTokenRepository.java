package com.providences.events.config.token;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    Set<RefreshTokenEntity> findAllByUserIdAndRevokedFalse(String userId);

    void deleteByToken(String token);
}
