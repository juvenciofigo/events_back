package com.providences.events.ticket.dto;

import java.time.LocalDateTime;

import com.providences.events.event.dto.SeatDTO;
import com.providences.events.ticket.entities.TicketEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class TicketDTO {

    public static class Request {
        public String EventId;
        public Integer totalPeople = 1;
        public String notes;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private String ticketCode;
        private String accessToken;
        private Integer totalPeople;
        private String notes;
        private TicketEntity.TicketStatus ticketStatus;
        private LocalDateTime sentAt;
        private LocalDateTime respondedAt;
        private SeatDTO.Response seat;

        public static Response response(TicketEntity ticket) {
            return new Response(
                    ticket.getTicketCode(),
                    ticket.getAccessToken(),
                    ticket.getTotalPeople(),
                    ticket.getNotes(),
                    ticket.getTicketStatus(),
                    ticket.getSentAt(),
                    ticket.getRespondedAt(),
                    SeatDTO.Response.response(ticket.getSeat()));
        }
    }
}
