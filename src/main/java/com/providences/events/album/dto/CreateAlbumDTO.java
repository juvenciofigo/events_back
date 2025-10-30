package com.providences.events.album.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CreateAlbumDTO {

    @Getter
    @Setter
    @Builder
    public static class Request {

        @NotBlank(message = "Escreva o título do album")
        private String title;

        private String description;

        private String serviceId;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String title;

        private String description;

        private String serviceId;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
