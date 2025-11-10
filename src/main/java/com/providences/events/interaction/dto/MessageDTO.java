package com.providences.events.interaction.dto;

import com.providences.events.interaction.entities.MessageEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        private String senderRole;
        
        @NotBlank(message = "Informe o remetente!")
        private String senderId;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String id;

        private String chatId;

        private String content;

        private String senderId;

        private String senderName;

        private String senderRole;

        public static Response response(MessageEntity message) {
            Response res = new Response();
            res.setId(message.getId());
            res.setChatId(message.getId());
            res.setContent(message.getContent());

            switch (message.getSender()) {
                case SUPPLIER:
                    res.setSenderId(message.getSenderSupplier().getId());
                    res.setSenderName(message.getSenderSupplier().getCompanyName());
                    res.setSenderRole("supplier");
                    break;

                case GUEST:
                    res.setSenderId(message.getSenderGuest().getId());
                    res.setSenderName(message.getSenderGuest().getName());
                    res.setSenderRole("guest");
                    break;

                case ORGANIZER:
                    res.setSenderId(message.getSenderOrganizer().getId());
                    res.setSenderName(message.getSenderOrganizer().getName());
                    res.setSenderRole("organizer");
                    break;

                default:
                    break;
            }
            return res;
        }
    }
}
