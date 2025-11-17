package com.providences.events.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.providences.events.user.dto.AuthUserDTO;
import com.providences.events.user.services.AuthUserService;

@RestController
@RequestMapping("/users/")
public class AuthUserController {

    private AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("login")
    public ResponseEntity<AuthUserDTO.Response> login(@Validated @RequestBody AuthUserDTO.Request request) {
        AuthUserDTO.Response response = authUserService.execute(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("test")
    @PreAuthorize("isAuthenticated()")
    public String getMethodName() {
        return "ola";
    }

}
