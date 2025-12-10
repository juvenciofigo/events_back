package com.providences.events.event.services.expense;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.ExpenseDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.ExpenseEntity;
import com.providences.events.event.entities.ExpenseEntity.ExpenseStatus;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchExpensesSummary {
    private EventRepository eventRepository;

    public FetchExpensesSummary(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public ExpenseDTO.Summary execute(String eventId, String userId) {

        EventEntity event = eventRepository.eventExpenses(eventId)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("User is not authorized to access this event", HttpStatus.FORBIDDEN);
        }

        Set<ExpenseEntity> expenses = event.getExpenses();

        int totalExpenses = expenses.size();

        int pendingExpenses = (int) expenses.stream()
                .filter(e -> e.getStatus() != null && e.getStatus().equals(ExpenseStatus.PENDING))
                .count();

        BigDecimal totalAmount = expenses.stream()
                .map(e -> e.getAmount() != null ? e.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseDTO.Summary(totalExpenses, pendingExpenses, totalAmount);
    }
}
