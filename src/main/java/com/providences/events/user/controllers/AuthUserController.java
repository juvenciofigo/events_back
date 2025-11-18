package com.providences.events.user.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.providences.events.user.dto.UserDTO;
import com.providences.events.user.services.AuthUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    private AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
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

        UserDTO.Response response = authUserService.login(request, ip, userAgent);

        resp.addHeader("Set-Cookie", response.getCookie());

        return ResponseEntity.ok().body(response);
    }

    // Refresh Token

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            HttpServletRequest req,
            HttpServletResponse resp,
            @CookieValue(value = "refreshToken", required = false) String cookieToken) {

        if (cookieToken == null) {
            return ResponseEntity.status(401).body("No refresh token");
        }

        UserDTO.Response response = authUserService.refreshToken(cookieToken, resp, req);

        resp.addHeader("Set-Cookie", response.getCookie());

        return ResponseEntity.ok(Map.of("accessToken", response.getToken()));
    }

    // Logout

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse resp,
            @CookieValue(value = "refreshToken", required = false) String cookieToken) {

        authUserService.logOut(cookieToken, resp);
        
        return ResponseEntity.ok().build();
    }

}
