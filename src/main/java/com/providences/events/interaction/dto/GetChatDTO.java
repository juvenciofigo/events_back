package com.providences.events.interaction.dto;

import com.providences.events.interaction.entities.ChatEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class GetChatDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String avatar;
        private String lastMessage;
        private java.time.LocalDateTime time;
        private Long unread;
        private String status;
        private String eventId;

        public static Response response(ChatEntity chat, String viewerId) {
            String lastMsg = chat.getMessages().stream()
                    .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                    .map(com.providences.events.interaction.entities.MessageEntity::getContent)
                    .orElse("");

            java.time.LocalDateTime updatedTime = chat.getUpdatedAt();

            // Unread count: count messages NOT read AND NOT sent by the viewer
            Long unreadCount = chat.getMessages().stream()
                    .filter(m -> !m.getIsRead())
                    .filter(m -> {
                        // If viewer sent it, it doesn't count as unread for the viewer
                        boolean isSenderSupplier = m.getSenderSupplier() != null
                                && m.getSenderSupplier().getId().equals(viewerId);
                        boolean isSenderOrganizer = m.getSenderOrganizer() != null
                                && m.getSenderOrganizer().getId().equals(viewerId);
                        boolean isSenderGuest = m.getSenderGuest() != null
                                && m.getSenderGuest().getId().equals(viewerId);

                        return !(isSenderSupplier || isSenderOrganizer || isSenderGuest);
                    })
                    .count();

            // Avatar: Using event cover image as default, or empty string
            String avatarStr = chat.getEvent().getCoverImage() != null ? chat.getEvent().getCoverImage() : "";

            return new Response(
                    chat.getId(),
                    chat.getTitle(),
                    avatarStr,
                    lastMsg,
                    updatedTime,
                    unreadCount,
                    "active", // Default status
                    chat.getEvent().getId());
        }
    }
}
