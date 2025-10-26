package com.providences.events.config;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.providences.events.user.UserEntity;

@Service
public class TokenService {

    private final String secret = "secret";

    public String generateToken(UserEntity user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(algorithm)
                    .build()
                    .verify(token);

            List<String> roles = decoded.getClaim("roles").asList(String.class);
            JWTUserData userData = new JWTUserData(
                    decoded.getClaim("userId").asString(),
                    decoded.getSubject(),
                    roles);

            return Optional.of(userData);

        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
