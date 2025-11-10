package com.providences.events.interaction.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.providences.events.interaction.entities.ChatEntity;
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
        private Set<ParticipantChatDTO.Response> participants;

        public static Response response(ChatEntity chat) {
            return new Response(
                    chat.getId(),
                    chat.getParticipants().stream()
                            .map(p -> ParticipantChatDTO.Response.response(p)).collect(Collectors.toSet()));
        }

        public static Response response(ChatEntity chat, Set<ParticipantChatDTO.Response> participants) {
            return new Response(
                    chat.getId(),
                    participants);
        }

    }
}
