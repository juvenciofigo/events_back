package com.providences.events.event.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CreateTaskDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Informa o evento")
        private String eventId;

        private String responsibleName;

        private String responsiblePhone;

        private String title;

        @NotBlank(message = "Informa a descricao da tarefa")
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dueDate;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String responsibleName;
        private String responsiblePhone;
        private String title;
        private String description;
        private LocalDateTime dueDate;
    }
}
