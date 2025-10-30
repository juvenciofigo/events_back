package com.providences.events.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.shared.dto.ApiResponse;
import com.providences.events.user.dto.AuthUserDTO;
import com.providences.events.user.dto.RegisterUserDTO;
import com.providences.events.user.services.CreateUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/users")
public class RegisterUserController {
    @Autowired
    private CreateUserService createUserService;

    @PostMapping(value = "/signup")
    public ResponseEntity<ApiResponse<AuthUserDTO.Response>> signup(
            @Validated @RequestBody RegisterUserDTO.Request dto) {
        AuthUserDTO.Response data = this.createUserService.execute(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<AuthUserDTO.Response>(true, data));
    }

}
