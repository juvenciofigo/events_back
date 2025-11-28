package com.providences.events.organizer.services.dasboard;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.ticket.entities.TicketEntity;

@Service
@Transactional
public class GetOrganizerStatsService {
        private OrganizerRepository organizerRepository;

        public GetOrganizerStatsService(OrganizerRepository organizerRepository) {
                this.organizerRepository = organizerRepository;
        }

        public DashboardOrganizerDTO.stats execute(String organizerId, String userId) {
                OrganizerEntity organizer = organizerRepository.statsOrganizer(organizerId)
                                .orElseThrow(() -> new BusinessException("Organizer not found", HttpStatus.NOT_FOUND));

                // Validation
                if (!organizer.getUser().getId().equals(userId)) {
                        throw new BusinessException("You are not authorized to access this organizer",
                                        HttpStatus.UNAUTHORIZED);
                }

                Set<EventEntity> events = organizer.getEvents();

                int totalEvents = events.size();

                Set<TicketEntity> tickets = events.stream()
                                .flatMap(e -> e.getTickets().stream())
                                .collect(Collectors.toSet());

                int ticketsSold = tickets.size();

                BigDecimal revenue = events.stream()
                                .flatMap(e -> e.getSeats().stream())
                                .filter(s -> Boolean.TRUE.equals(s.getIsPaid())) // Apenas assentos pagos
                                .filter(s -> s.getPayments() != null) // Evitar NPE
                                .flatMap(s -> s.getPayments().stream())
                                .map(PaymentEntity::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                int guests = tickets.stream()
                                .mapToInt(TicketEntity::getTotalPeople)
                                .sum();

                return new DashboardOrganizerDTO.stats(totalEvents, ticketsSold, revenue, guests);
        }
}
