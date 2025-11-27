package com.providences.events.organizer.dto;

import java.math.BigDecimal;

public record OrganizerStatsDTO(
        int totalEvents,
        int ticketsSold,
        BigDecimal revenue,
        int guests) {
}