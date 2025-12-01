package com.providences.events.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.providences.events.event.entities.EventEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EventDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        private String title;

        @NotBlank(message = "Informe o tipo de evento")
        private String category;

        private Boolean isPublic;

        @NotNull(message = "Informe a data de início do evento")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime dateStart;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime dateEnd;

        private String description;

        @PositiveOrZero(message = "O numero de convidados deve ser zero ou mais")
        private Integer estimatedGuest;

        @PositiveOrZero(message = "O valor estimado deve ser zero ou mais")
        private BigDecimal budgetEstimated;

        @PositiveOrZero(message = "O valor estimado deve ser zero ou mais")
        private BigDecimal budgetSpent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        private String title;
        private String category;
        private Boolean isPublic;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dateStart;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dateEnd;

        private String coverImage;
        private String description;

        @PositiveOrZero(message = "O numero de convidados deve ser zero ou mais")
        private Integer estimatedGuest;

        @PositiveOrZero(message = "O numero de convidados deve ser zero ou mais")
        private BigDecimal budgetEstimated;

        @PositiveOrZero(message = "O numero de convidados deve ser zero ou mais")
        private BigDecimal budgetSpent;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String type;
        private Boolean isPublic;
        private LocalDateTime dateStart;
        private LocalDateTime dateEnd;
        private String coverImage;
        private String description;
        private Integer estimatedGuest;
        private BigDecimal budgetEstimated;
        private BigDecimal budgetSpent;

        public static Response response(EventEntity event) {
            return new Response(
                    event.getId(),
                    event.getTitle(),
                    event.getCategory(),
                    event.getIsPublic(),
                    event.getDateStart(),
                    event.getDateEnd(),
                    event.getCoverImage(),
                    event.getDescription(),
                    event.getEstimatedGuest(),
                    event.getBudgetEstimated(),
                    event.getBudgetSpent());
        }
    }
}
