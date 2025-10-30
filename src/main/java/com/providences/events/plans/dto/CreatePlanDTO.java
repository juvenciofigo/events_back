package com.providences.events.plans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreatePlanDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Informe o nome do plano")
        private String name;
        
        private String description;

        private String resources;

        @NotNull(message = "Informe o preço mensal")
        private Double priceMonthly;

        private Double priceYearly;

        private String features;
        
        private Integer level;
        
        @NotBlank(message = "Informe o tipo de plano")
        private String planType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String id;
        private String name;
        private String description;
        private String resources;
        private Double priceMonthly;
        private Double priceYearly;
        private String features;
        private Integer level;
    }
    
}
