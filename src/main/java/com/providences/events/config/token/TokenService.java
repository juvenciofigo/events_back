package com.providences.events.config.token;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;

@Service
public class TokenService {

    private final String secret;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final long accessExpMinutes;
    private final long refreshExpDays;

    public TokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-expiration-minutes}") long accessExpMinutes,
            @Value("${app.jwt.refresh-expiration-days}") long refreshExpDays) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.secret = secret;
        this.accessExpMinutes = accessExpMinutes;
        this.refreshExpDays = refreshExpDays;
    }

    public String generateToken(UserEntity user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plus(accessExpMinutes, ChronoUnit.MINUTES))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserDTO> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(algorithm)
                    .build()
                    .verify(token);

            Set<String> roles = new HashSet<>(decoded.getClaim("roles").asList(String.class));
            JWTUserDTO userData = new JWTUserDTO(decoded.getClaim("userId").asString(), decoded.getSubject(), roles);

            return Optional.of(userData);
        } catch (TokenExpiredException ex) {
            throw new BusinessException("Token expired", HttpStatus.valueOf(401));
        } catch (JWTVerificationException e) {
            throw new BusinessException("Invalid token", HttpStatus.valueOf(401));
        }
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(String userId, String ip, String userAgent) {

        UserEntity user = userRepository.findId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(refreshExpDays));
        token.setRevoked(false);
        token.setIp(ip);
        token.setUserAgent(userAgent);
        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(RefreshTokenEntity t) {
        return t.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void revoke(RefreshTokenEntity t) {
        t.setRevoked(true);
        refreshTokenRepository.save(t);
    }

    public void delete(RefreshTokenEntity t) {
        refreshTokenRepository.delete(t);
    }

    public void deleteAllUserTokens(String userId) {
        var list = refreshTokenRepository.findAllByUserIdAndRevokedFalse(userId);
        for (var t : list) {
            t.setRevoked(true);
        }
        refreshTokenRepository.saveAll(list);
    }

    public long getRefreshExpirationSeconds() {
        return refreshExpDays * 24 * 60 * 60; // dias -> segundos
    }

}
