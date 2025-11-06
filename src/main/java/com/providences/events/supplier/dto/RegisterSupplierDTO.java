package com.providences.events.supplier.dto;

import com.providences.events.supplier.SupplierEntity;
import com.providences.events.user.dto.AuthUserDTO;

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
    @AllArgsConstructor
    public static class Response {
        private String id;

        private String companyName;

        private String profilePicture;

        private String logo;

        private String description;

        private AuthUserDTO.Response user;

        public static Response reponse(SupplierEntity supplier, AuthUserDTO.Response user) {
            return new Response(
                    supplier.getId(),
                    supplier.getCompanyName(),
                    supplier.getProfilePicture(),
                    supplier.getLogo(),
                    supplier.getDescription(),
                    user);
        }
    }

}
