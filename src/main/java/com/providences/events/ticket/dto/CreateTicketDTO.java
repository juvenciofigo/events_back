package com.providences.events.ticket.dto;

import java.math.BigDecimal;

public class CreateTicketDTO {

    public static class Request {
        public String EventId;
        public Integer totalPeople = 1;
        public String notes;
        public BigDecimal pricePaid = new BigDecimal(0);

    }

    public static class Response {

    }
}
