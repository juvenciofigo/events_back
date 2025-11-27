package com.providences.events.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.user.dto.UserDTO;
import com.providences.events.user.services.CreateUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    public ResponseEntity<UserDTO.Response> signup(
            @Validated @RequestBody UserDTO.Create data,
            HttpServletRequest req,
            HttpServletResponse resp) {
        String ip = req.getRemoteAddr();
        String userAgent = req.getHeader("User-Agent");

        UserDTO.Response response = this.createUserService.execute(data, ip, userAgent, resp);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
