package com.providences.events.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateEventDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Informe o tipo de evento")
        private String type;

        private Boolean isPublic = false;

        private String title;

        @NotNull(message = "Informe a data de início do evento")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // ✅ permite usar espaço em vez de "T"
        private LocalDateTime dateStart;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dateEnd;

        private String coverImage;

        private String description;

        @PositiveOrZero(message = "O numero de convidados deve ser zero ou mais")
        private Integer estimatedGuest;

        private BigDecimal budgetEstimated;

        private BigDecimal budgetSpent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String id;
        private String type;
        private Boolean isPublic;
        private String title;
        private LocalDateTime dateStart;
        private LocalDateTime dateEnd;
        private String coverImage;
        private String description;
        private Integer estimatedGuest;
        private BigDecimal budgetEstimated;
        private BigDecimal budgetSpent;
    }
}
