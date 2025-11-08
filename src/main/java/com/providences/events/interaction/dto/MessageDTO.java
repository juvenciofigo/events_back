package com.providences.events.interaction.dto;

import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.interaction.entities.MessageEntity;
import com.providences.events.organizer.dto.RegisterOrganizerDTO;
import com.providences.events.supplier.dto.RegisterSupplierDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MessageDTO {

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Insira o chat!")
        private String chatId;

        @NotBlank(message = "Nao pode enviar mensagem vazia!")
        private String content;

        @NotBlank(message = "Quem envia mensagem!")
        private String sender;

        private String senderSupplierId;

        private String senderOrganizerId;

        private String senderGuestId;

        private Boolean senderAdmin;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {

        private String id;

        private String chatId;

        private String content;

        private String sender;

        private RegisterSupplierDTO.Response senderSupplier;

        private RegisterOrganizerDTO.Response senderOrganizer;

        private CreateGuestDTO.Response senderGuest;

        private Boolean senderAdmin;

        public static Response response(MessageEntity message) {
            return new Response(
                    message.getId(),
                    message.getChat().getId(),
                    message.getContent(),
                    message.getSender().name(),
                    message.getSenderSupplier() != null
                            ? RegisterSupplierDTO.Response.response2(message.getSenderSupplier())
                            : null,
                    message.getSenderOrganizer() != null
                            ? RegisterOrganizerDTO.Response.response2(message.getSenderOrganizer())
                            : null,
                    message.getSenderGuest() != null ? CreateGuestDTO.Response.response2(message.getSenderGuest())
                            : null,
                    message.isSenderAdmin());
        }
    }
}
