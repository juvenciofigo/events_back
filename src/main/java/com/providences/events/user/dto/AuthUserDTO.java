package com.providences.events.user.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class AuthUserDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "O campo email é obrigatário!")
        @NotBlank(message = "Preencha o campo email")
        private String email;

        @NotNull(message = "O campo password é obrigatário!")
        @NotBlank(message = "Preencha o campo password")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String email;
        private String name;
        private String profilePicture;
        private List<String> roles;
        private String token;

    }

}
