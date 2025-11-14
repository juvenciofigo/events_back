package com.providences.events.services.dto;

import java.math.BigDecimal;

import com.providences.events.services.ServiceEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ServiceDTO {

    @Data
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Informe o fornecedo")
        private String supplierId;

        @NotBlank(message = "Informa a categoria")
        private String category;

        private String description = "";

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "Preço base precisa ser maior que zero")
        private BigDecimal priceBase;
    }

    @Data
    @AllArgsConstructor
    public static class Update {

        @NotBlank(message = "Informa a categoria")
        private String category;

        private String description = "";

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "Preço base precisa ser maior que zero")
        private BigDecimal priceBase;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String category;
        private String description;
        private BigDecimal priceBase;

        public static Response response(ServiceEntity data) {
            return new Response(
                    data.getId(),
                    data.getCategory(),
                    data.getDescription(),
                    data.getPriceBase());

        }
    }
}
