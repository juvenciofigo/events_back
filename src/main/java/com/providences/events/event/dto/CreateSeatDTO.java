package com.providences.events.event.dto;

import java.math.BigDecimal;

import com.providences.events.event.entities.SeatEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateSeatDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Informe o nome desse espaço")
        private String name;

        private String description;

        @NotNull(message = "Infome o número de lugares dispoíveis")
        private Integer totalSeats;

        @NotBlank(message = "Infome o evento")
        private String eventId;

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

        private Integer availableSeats;

        private Double layoutPositionX;

        private Double layoutPositionY;

        public static CreateSeatDTO.Response response(SeatEntity seat) {
            return new CreateSeatDTO.Response(
                    seat.getId(),
                    seat.getName(),
                    seat.getDescription(),
                    seat.getTotalSeats(),
                    seat.getAvailableSeats(),
                    seat.getLayoutPositionX(),
                    seat.getLayoutPositionY());
        }
    }
}
