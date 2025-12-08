package com.providences.events.event.services.expense;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.ExpenseDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.ExpenseEntity;
import com.providences.events.event.entities.ExpenseEntity.ExpensePaymentStatus;
import com.providences.events.event.entities.ExpenseEntity.ExpenseStatus;
import com.providences.events.event.entities.ExpenseEntity.Priority;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.ExpenseRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class CreateExpenseService {
    private ExpenseRepository expenseRepository;
    private EventRepository eventRepository;

    public CreateExpenseService(ExpenseRepository expenseRepository, EventRepository eventRepository) {
        this.expenseRepository = expenseRepository;
        this.eventRepository = eventRepository;
    }

    public ExpenseDTO.Response createExpense(ExpenseDTO.Create data, String eventId, String userId) {
        EventEntity event = eventRepository.findId(eventId)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("Sem autorizacao para criar despensa!", HttpStatus.FORBIDDEN);
        }

        ExpenseEntity expense = new ExpenseEntity();

        ExpensePaymentStatus paymentStatus;
        try {
            paymentStatus = ExpensePaymentStatus.valueOf(data.getPaymentStatus().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de status de pagamento invalido!", HttpStatus.BAD_REQUEST);
        }

        Priority priority;
        try {
            priority = Priority.valueOf(data.getPriority().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de prioridade invalido!", HttpStatus.BAD_REQUEST);
        }

        ExpenseStatus status;
        try {
            status = ExpenseStatus.valueOf(data.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de status invalido!", HttpStatus.BAD_REQUEST);
        }

        expense.setEvent(event);
        expense.setTitle(data.getTitle() != null ? data.getTitle() : "");
        expense.setCategory(data.getCategory() != null ? data.getCategory() : "");
        expense.setDescription(data.getDescription() != null ? data.getDescription() : "");
        expense.setAmount(data.getAmount() != null ? BigDecimal.valueOf(data.getAmount()) : BigDecimal.ZERO);
        expense.setDueDate(data.getDueDate() != null ? data.getDueDate() : null);
        expense.setPaymentStatus(paymentStatus);
        expense.setPriority(priority);
        expense.setStatus(status);

        ExpenseEntity createdExpense = expenseRepository.save(expense);

        return ExpenseDTO.Response.response(createdExpense);
    }
}
