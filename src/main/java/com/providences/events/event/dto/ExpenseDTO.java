package com.providences.events.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.providences.events.event.entities.ExpenseEntity;

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

        private String title;
        private String category;
        private String description;
        private String priority;
        private String status;
        private String paymentStatus;

        @PositiveOrZero(message = "O valor estimado deve ser zero ou mais")
        private Double amount;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
        private String priority;
        private String status;
        private String paymentStatus;
        private LocalDateTime dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response response(ExpenseEntity expense) {
            return new Response(
                    expense.getId(),
                    expense.getTitle() != null ? expense.getTitle() : null,
                    expense.getCategory() != null ? expense.getCategory() : null,
                    expense.getDescription() != null ? expense.getDescription() : null,
                    expense.getAmount() != null ? expense.getAmount() : null,
                    expense.getPriority() != null ? expense.getPriority().toString() : null,
                    expense.getStatus() != null ? expense.getStatus().toString() : null,
                    expense.getPaymentStatus() != null ? expense.getPaymentStatus().toString() : null,
                    expense.getDueDate() != null ? expense.getDueDate() : null,
                    expense.getCreatedAt(),
                    expense.getUpdatedAt());
        }
    }
}
