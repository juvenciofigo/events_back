package com.providences.events.event.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreataSeatsDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Informe o nome desse espaço")
        private String name;

        private String description;

        @NotBlank(message = "Infome o número de lugares dispoíveis")
        private Integer totalSeats;

        @NotBlank(message = "Infome o evento")
        private String eventId;

        private Double layoutPositionX;
        private Double layoutPositionY;
    }

    @Data
    @AllArgsConstructor
    public static class Response {

        private String id;

        private String name;

        private String description;

        private Integer totalSeats;

        private Double layoutPositionX;

        private Double layoutPositionY;
    }
}
