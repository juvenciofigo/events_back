package com.providences.events.organizer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardOrganizerDTO {

        public DashboardOrganizerDTO() {

        }

        public static record ItemWithPage<T>(
                        List<T> items,
                        int page,
                        int totalPages,
                        long totalItems) {
        }

        public static record stats(
                        int totalEvents,
                        int ticketsSold,
                        BigDecimal revenue,
                        int guests,
                        boolean hasEvents,
                        boolean hasPaidEvents) {
        }

        public static record sales(String day, BigDecimal sales) {
        }

        public static record tasks(
                        String id,
                        String title,
                        String event,
                        String description,
                        String priority,
                        String taskStatus) {
        }

        public static record upcomingEvent(
                        String id,
                        String title,
                        String date,
                        Integer estimatedGuest,
                        String location) {
        }

        public static record reviews(
                        String id,
                        String senderType,
                        String senderName,
                        String senderId,
                        String comment,
                        Double rating,
                        LocalDateTime createdAt) {
        }

}
