package com.providences.events.album.dto;

import java.time.LocalDateTime;

import com.providences.events.album.entities.MediaEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AddMediaAlbumDto {

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;

        private String fileUrl;

        private String mediaType;

        private LocalDateTime createdAt;

        public static Response response(MediaEntity media) {
            return new Response(
                    media.getId(),
                    media.getFileUrl(),
                    media.getMediaType().name(),
                    media.getCreatedAt());

        }
    }
}
