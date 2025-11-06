package com.providences.events.interaction.dto;

import java.util.List;

import com.providences.events.interaction.entities.ChatEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CreateChatDTO {

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Informa o tipo de conversa")
        private String type;

        @NotBlank(message = "Informa id do evento")
        private String eventId;

        @NotBlank(message = "Informa título da conversa")
        private String title;

        private String organizerId;

        private String supplierId;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private List<ParticipantChatEntity> participants;

        public static Response response(ChatEntity chat) {
            return new Response(
                    chat.getId(),
                    chat.getParticipants());
        }
    }
}
