package com.providences.events.supplier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterSupplierDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "O campo nome da empresa é obrigatário!")
        @NotBlank(message = "Preencha o campo nome da empresa")
        private String companyName;

        private String profilePicture = "";

        private String logo = "";

        private String description = "";

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;

        private String companyName;

        private String profilePicture;

        private String logo;

        private String description;

        private UserDTO user;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private String id;
        private String name;
        private String email;
        private String profilePicture;
    }
}
