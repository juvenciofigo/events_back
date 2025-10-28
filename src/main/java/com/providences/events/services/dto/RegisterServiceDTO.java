package com.providences.events.services.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterServiceDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        @NotNull
        private String supplierId;

        @NotBlank
        @NotNull
        private String category;
        
        private String description="";

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "Preço base precisa ser maior que zero")
        private BigDecimal priceBase;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String id;
        private String category;
        private String description;
        private BigDecimal priceBase;

    }
}
