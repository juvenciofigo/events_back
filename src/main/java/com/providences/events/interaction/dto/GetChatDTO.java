package com.providences.events.interaction.dto;

import com.providences.events.interaction.entities.ChatEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GetChatDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private String id;

        private String title;

        private String eventId;

        public static Response response(ChatEntity chat) {
            return new Response(
                    chat.getId(),
                    chat.getTitle(),
                    chat.getEvent().getId());
        }
    }
}
