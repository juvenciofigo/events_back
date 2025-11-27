package com.providences.events.interaction.dto;

import com.providences.events.interaction.entities.ParticipantChatEntity;
import com.providences.events.interaction.entities.ParticipantChatEntity.ParticipantType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ParticipantChatDTO {
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {

        private String id;
        private ParticipantType type;
        private String name;

        // construtor
        public static Response response(ParticipantChatEntity participant) {
            return new Response(
                    participant.getId(),
                    participant.getType(),
                    getName(participant.getType(), participant));
        }

        private static String getName(ParticipantType type, ParticipantChatEntity participant) {
            switch (type.name()) {
                case "SUPPLIER":
                    return participant.getSupplier().getCompanyName();
                case "GUEST":
                    return participant.getGuest().getName();
                case "ORGANIZER":
                    return participant.getOrganizer().getCompanyName();
                case "ADMIN":
                    return "ADMIN";
                default:
                    return null;
            }
        }
    }
}
