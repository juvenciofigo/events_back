package com.providences.events.organizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterOrganizerDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "O campo nome é obrigatário!")
        @NotBlank(message = "Preencha o campo nome")
        @Size(max = 100, min = 3, message = "O nome deve ter de 3 à 100 caracteres!")
        private String name;

        private String profilePicture;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String profilePicture;
        private UserDTO user;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDTO {
        private String id;
        private String name;
        private String email;
        private String profilePicture;
    }
}
