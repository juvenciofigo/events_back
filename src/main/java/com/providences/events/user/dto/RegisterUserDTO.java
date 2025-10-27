package com.providences.events.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class RegisterUserDTO {

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

        @NotNull(message = "O campo password é obrigatário!")
        @NotBlank(message = "Preencha o campo password")
        @Size(max = 100, min = 3, message = "O nome deve ter de 3 à 100 caracteres!")
        private String name;

        @NotNull(message = "O campo telefone é obrigatário!")
        @NotBlank(message = "Preencha o campo telefone")
        private String phone;
    }

}
