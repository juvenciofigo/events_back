package com.providences.events.user.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.providences.events.config.token.RefreshTokenEntity;
import com.providences.events.config.token.TokenService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CreateRefreshToken {

    private final boolean cookieSecure;
    private final TokenService tokenService;

    public CreateRefreshToken(
            @Value("${app.cookie.secure}") boolean cookieSecure,
            TokenService tokenService) {
        this.cookieSecure = cookieSecure;
        this.tokenService = tokenService;
    }

    public String execute(String userId, String ip, String userAgent) {
        // Criar Refresh token
        RefreshTokenEntity refreshToken = tokenService.createRefreshToken(userId, ip, userAgent);

        // criar cookie httpOnly com refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(cookieSecure) // em dev pode ser false, em produção true
                .path("/auth/refresh") // só será enviado para endpoint /auth/refresh (opcional)
                .maxAge(tokenService.getRefreshExpirationSeconds()) // expira no cliente
                .sameSite("None") // None para cross-site, ou Lax/Strict
                .build();

        return cookie.toString();
    }

    public void clearRefreshCookie(HttpServletResponse resp) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/auth/refresh")
                .maxAge(0)
                .sameSite("None")
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }
}
