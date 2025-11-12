package com.providences.events.guest.dto;

import com.providences.events.guest.GuestEntity;
import com.providences.events.ticket.dto.CreateTicketDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GuestDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {
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
    public static class Update {
        @NotBlank(message = "Prencha o nome!")
        @NotNull(message = "Prencha o nome!")
        private String name;

        @Email(message = "Email inválido!")
        private String email;

        @NotBlank(message = "Prencha o número de telefone")
        @NotNull(message = "Prencha o número de telefone")
        private String phone;

        @NotBlank(message = "Informe o guestId")
        @NotNull(message = "Informe o guestId")
        private String guestId;

        @Positive(message = "O número de pessoas deve ser maior que zero")
        @NotNull(message = "Informe o numero de pessoas para esse ticket")
        private Integer totalPeople;

        private String notes;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private String email;
        private String phone;
        private CreateTicketDTO.Response ticket;

        public static Response response(GuestEntity guest) {
            return new Response(
                    guest.getId(),
                    guest.getName(),
                    guest.getEmail(),
                    guest.getPhone(),
                    CreateTicketDTO.Response.response(guest.getTicket()));
        }
    }

}
