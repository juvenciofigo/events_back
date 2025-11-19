package com.providences.events.user.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.providences.events.user.UserEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class UserDTO {

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Auth {
        @NotNull(message = "O campo email é obrigatário!")
        @NotBlank(message = "Preencha o campo email")
        private String email;

        @NotNull(message = "O campo password é obrigatário!")
        @NotBlank(message = "Preencha o campo password")
        private String password;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Create {
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

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String email;
        private String name;
        private Set<String> roles;
        private String token;
        private String cookie;
        private Map<String, String> profiles;

        public static Response response(UserEntity user, String token, String cookie) {
            Map<String, String> profiles = new HashMap<>();
            if (user.getOrganizer() != null) {
                profiles.put("organizer", user.getOrganizer().getId());
            }
            if (user.getSupplier() != null) {
                profiles.put("supplier", user.getSupplier().getId());
            }

            return new Response(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toSet()),
                    token != null ? token : null,
                    cookie,
                    profiles);
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Profiles {
        private String organizer;
        private String supplier;
        private String guest;
    }
}
