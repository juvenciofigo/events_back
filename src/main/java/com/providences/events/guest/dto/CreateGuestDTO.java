package com.providences.events.guest.dto;

import com.providences.events.ticket.dto.CreateTicketDTO;
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

        private String seatId;

        // pagamento
        public String payerNum;
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
        private CreateTicketDTO.Response ticket;
    }
}
