package com.providences.events.event.dto;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import com.providences.events.event.entities.SeatEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SeatDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "Informe o nome desse espaço")
        private String name;

        private String description;

        @NotNull(message = "Informe o número de lugares disponíveis")
        private Integer totalSeats;

        @NotNull(message = "Informe se o assento é pago")
        private Boolean isPaid = false;

        private BigDecimal price;

        private Double layoutPositionX;
        private Double layoutPositionY;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        @NotBlank(message = "Informe o nome desse espaço")
        private String name;

        private String description;

        @NotNull(message = "Informe o número de lugares disponíveis")
        private Integer totalSeats;

        @NotNull(message = "Informe se o assento é pago")
        private Boolean isPaid;

        private BigDecimal price;

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
        private Boolean isPaid;
        private BigDecimal price;
        private Integer availableSeats;
        private Double layoutPositionX;
        private Double layoutPositionY;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static SeatDTO.Response response(SeatEntity seat) {
            return new SeatDTO.Response(
                    seat.getId(),
                    seat.getName(),
                    seat.getDescription(),
                    seat.getTotalSeats(),
                    seat.getIsPaid(),
                    seat.getPrice() != null ? seat.getPrice() : null,
                    seat.getAvailableSeats(),
                    seat.getLayoutPositionX(),
                    seat.getLayoutPositionY(),
                    seat.getCreatedAt(),
                    seat.getUpdatedAt());
        }
    }
}
