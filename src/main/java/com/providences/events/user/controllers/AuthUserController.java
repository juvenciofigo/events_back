package com.providences.events.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.providences.events.user.dto.AuthUserDTO;
import com.providences.events.user.services.AuthUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
public class AuthUserController {

    @Autowired
    private AuthUserService authUserService;

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
