package com.providences.events.ticket.dto;

import java.time.LocalDateTime;

import com.providences.events.event.dto.CreataSeatDTO;
import com.providences.events.ticket.entities.TicketEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CreateTicketDTO {

    public static class Request {
        public String EventId;
        public Integer totalPeople = 1;
        public String notes;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private String code;
        private Integer totalPeople;
        private String notes;
        private TicketEntity.Status status;
        private LocalDateTime sentAt;
        private LocalDateTime respondedAt;
        private CreataSeatDTO.Response seat;
    }
}
