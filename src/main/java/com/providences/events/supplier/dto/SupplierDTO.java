package com.providences.events.supplier.dto;

import com.providences.events.supplier.SupplierEntity;
import com.providences.events.user.dto.UserDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SupplierDTO {

    @Data
    @AllArgsConstructor
    public static class Create {
        @NotBlank(message = "Preencha o campo nome da empresa")
        private String companyName;

        private String description;

    }

    @Data
    @AllArgsConstructor
    public static class Update {
        @NotBlank(message = "Preencha o campo nome da empresa")
        private String companyName;

        private String description;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String id;

        private String companyName;

        private String profilePicture;

        private String logo;

        private String description;

        private UserDTO.Response user;

        public static Response response(SupplierEntity supplier, UserDTO.Response user) {
            return new Response(
                    supplier.getId(),
                    supplier.getCompanyName(),
                    supplier.getProfilePicture(),
                    supplier.getLogo(),
                    supplier.getDescription(),
                    user);
        }

        public static Response response2(SupplierEntity supplier) {
            Response res = new Response();
            res.setId(supplier.getId());
            res.setCompanyName(supplier.getCompanyName());
            res.setProfilePicture(supplier.getProfilePicture());
            res.setLogo(supplier.getLogo());
            res.setDescription(supplier.getDescription());

            return res;
        }
    }

}
