package com.providences.events.user.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.user.dto.UserDTO;
import com.providences.events.user.services.AuthUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthUserService authUserService;

    public AuthController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    // Login

    @PostMapping("/login")
    public ResponseEntity<UserDTO.Response> login(
            @Validated @RequestBody UserDTO.Auth request,
            HttpServletRequest req,
            HttpServletResponse resp) {

        String ip = req.getRemoteAddr();
        String userAgent = req.getHeader("User-Agent");

        UserDTO.Response response = authUserService.login(request, ip, userAgent, resp);

        return ResponseEntity.ok().body(response);
    }

    // Refresh Token

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            HttpServletRequest req,
            HttpServletResponse resp,
            @CookieValue(value = "refreshToken", required = false) String cookieRefreshToken) {

        if (cookieRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token missing"));
        }

        try {
            UserDTO.Response response = authUserService.refreshToken(cookieRefreshToken, resp, req);
            return ResponseEntity.ok(Map.of("accessToken", response.getToken()));

        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getMessage()));
        }
    }
    // Logout

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse resp,
            @CookieValue(value = "refreshToken", required = false) String cookieToken) {

        authUserService.logOut(cookieToken, resp);

        return ResponseEntity.ok().build();
    }

}
