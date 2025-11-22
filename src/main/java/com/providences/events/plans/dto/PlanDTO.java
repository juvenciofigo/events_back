package com.providences.events.plans.dto;

import com.providences.events.plans.entities.PlanEntity;
import com.providences.events.plans.entities.PlanEntity.PlanType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PlanDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

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

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private String description;
        private String resources;
        private Double priceMonthly;
        private Double priceYearly;
        private String features;
        private Integer level;
        private PlanType planType;

        public static Response response(PlanEntity plan) {
            return new Response(
                    plan.getId(),
                    plan.getName(),
                    plan.getDescription(),
                    plan.getResources(),
                    plan.getPriceMonthly(),
                    plan.getPriceYearly(),
                    plan.getFeatures(),
                    plan.getLevel(),
                    plan.getPlanType());

        }

    }

}
