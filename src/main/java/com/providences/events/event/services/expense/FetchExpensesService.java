package com.providences.events.event.services.expense;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.ExpenseDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.ExpenseEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.ExpenseRepository;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchExpensesService {
    private ExpenseRepository expenseRepository;
    private EventRepository eventRepository;

    public FetchExpensesService(ExpenseRepository expenseRepository, EventRepository eventRepository) {
        this.expenseRepository = expenseRepository;
        this.eventRepository = eventRepository;
    }

    public SystemDTO.ItemWithPage<ExpenseDTO.Response> fetchExpenses(String eventId, String userId, int pageNumber,
            int limit, String sort) {

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("User is not authorized to access this event", HttpStatus.FORBIDDEN);
        }

        int validPage = Math.max(pageNumber - 1, 0);
        PageRequest pageRequest = PageRequest.of(validPage, limit, Sort.by(sort).descending());

        Page<ExpenseEntity> expenses = expenseRepository.findByEventIdPageable(eventId, pageRequest);

        List<ExpenseDTO.Response> list = expenses.stream()
                .map(e -> ExpenseDTO.Response.response(e))
                .toList();

        return new SystemDTO.ItemWithPage<>(
                list,
                expenses.getNumber(),
                expenses.getTotalPages(),
                expenses.getTotalElements());
    }
}
