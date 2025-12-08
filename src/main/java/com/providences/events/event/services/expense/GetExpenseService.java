package com.providences.events.event.services.expense;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.ExpenseDTO;
import com.providences.events.event.entities.ExpenseEntity;
import com.providences.events.event.repositories.ExpenseRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class GetExpenseService {
    private ExpenseRepository expenseRepository;

    public GetExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseDTO.Response execute(String expenseId, String userId) {
        ExpenseEntity expense = expenseRepository.findId(expenseId)
                .orElseThrow(() -> new BusinessException("Expense not found", HttpStatus.NOT_FOUND));

        if (!expense.getEvent().getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("User is not authorized to access this expense", HttpStatus.FORBIDDEN);
        }

        return ExpenseDTO.Response.response(expense);

    }
}
