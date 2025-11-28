package com.providences.events.organizer.services.dasboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

@Service
@Transactional(readOnly = true)
public class GetSalesOrganizerService {
    private final OrganizerRepository organizerRepository;

    public GetSalesOrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    public List<DashboardOrganizerDTO.sales> execute(String organizerId, String userId) {
        OrganizerEntity organizer = organizerRepository.statsOrganizer(organizerId)
                .orElseThrow(() -> new BusinessException("Organizer not found", HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to access this organizer", HttpStatus.UNAUTHORIZED);
        }

        Set<EventEntity> events = organizer.getEvents();

        // Obter todos os pagamentos válidos
        List<PaymentEntity> payments = events.stream()
                .flatMap(e -> e.getSeats().stream())
                .filter(s -> Boolean.TRUE.equals(s.getIsPaid()))
                .filter(s -> s.getPayments() != null)
                .flatMap(s -> s.getPayments().stream())
                .collect(Collectors.toList());

        Map<LocalDate, BigDecimal> salesByDay = payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCreatedAt().toLocalDate(), // chave: dia
                        Collectors.mapping(
                                PaymentEntity::getAmount, // valores: amount
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        return salesByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DashboardOrganizerDTO.sales(entry.getKey().toString(), entry.getValue()))
                .collect(Collectors.toList());

    }
}
