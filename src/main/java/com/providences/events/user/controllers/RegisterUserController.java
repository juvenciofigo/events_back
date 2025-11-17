package com.providences.events.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.user.dto.AuthUserDTO;
import com.providences.events.user.dto.RegisterUserDTO;
import com.providences.events.user.services.CreateUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class RegisterUserController {
    private CreateUserService createUserService;

    public RegisterUserController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthUserDTO.Response> signup(
            @Validated @RequestBody RegisterUserDTO.Request dto) {
        AuthUserDTO.Response data = this.createUserService.execute(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

}
