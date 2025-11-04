package com.providences.events.guest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.providences.events.ticket.entities.TicketEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CreateGuestDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Prencha o nome!")
        @NotNull(message = "Prencha o nome!")
        private String name;

        @Email(message = "Email inválido!")
        private String email;

        @NotBlank(message = "Prencha o número de telefone")
        @NotNull(message = "Prencha o número de telefone")
        private String phone;

        @NotBlank(message = "Informe o evento")
        @NotNull(message = "Informe o evento")
        private String eventId;

        @Positive(message = "O número de pessoas deve ser maior que zero")
        @NotNull(message = "Informe o numero de pessoas para esse ticket")
        private Integer totalPeople;

        private String notes;

        private String seat;

        // pagamento
        @NotNull(message = "Informe se o convite é paga ou nao")
        public Boolean isPayd;
        public String payerNum;
        private BigDecimal pricePaid;
        public String paymentMethod;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private String email;
        private String phone;
        private String code;
        private Integer totalPeople;
        private String notes;
        private LocalDateTime sentAt;
        private LocalDateTime respondedAt;
        private String seat;
        private TicketEntity.Status status;

    }
}
