package com.providences.events.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.providences.events.event.entities.ExpenseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ExpenseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "Infome o evento")
        private String eventId;

        private String title;
        private String category;
        private String description;

        @PositiveOrZero(message = "O valor estimado deve ser zero ou mais")
        private BigDecimal amount;

        private String paymentStatus;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dueDate;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        private String id;
        private String title;
        private String category;
        private String description;
        private BigDecimal amount;
        private String paymentStatus;
        private LocalDateTime dueDate;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String category;
        private String description;
        private BigDecimal amount;
        private LocalDateTime dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response response(ExpenseEntity expense) {
            return new Response(
                expense.getId(),
                expense.getTitle(),
                expense.getCategory(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDueDate(),
                expense.getCreatedAt(),
                expense.getUpdatedAt());
        }
    }
}
