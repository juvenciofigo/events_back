package com.providences.events.event.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.providences.events.event.entities.TaskEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class TaskDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "Informa o evento")
        private String eventId;

        private String responsibleName;

        private String responsiblePhone;

        private String title;

        @NotBlank(message = "Informa a descricao da tarefa")
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime dueDate;

        @JsonProperty(defaultValue = "MEDIUM")
        private String priority;

        @JsonProperty(defaultValue = "PENDING")
        private String taskStatus;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Update {

        private String responsibleName;

        private String responsiblePhone;

        private String title;

        @JsonProperty(defaultValue = "MEDIUM")
        private String priority;

        @JsonProperty(defaultValue = "PENDING")
        private String taskStatus;

        @NotBlank(message = "Informa a descricao da tarefa")
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
        private String eventTitle;
        private String description;
        private String priority;
        private String taskStatus;
        private LocalDateTime dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response response(TaskEntity task) {
            return new Response(
                    task.getId(),
                    task.getResponsibleName(),
                    task.getResponsiblePhone(),
                    task.getTitle(),
                    task.getEvent().getTitle(),
                    task.getDescription(),
                    task.getPriority().name(),
                    task.getTaskStatus().name(),
                    task.getDueDate(),
                    task.getCreatedAt(),
                    task.getUpdatedAt());
        }
    }
}
