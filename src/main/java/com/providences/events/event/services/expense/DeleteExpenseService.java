package com.providences.events.event.services.expense;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.ExpenseEntity;
import com.providences.events.event.repositories.ExpenseRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class DeleteExpenseService {
    private ExpenseRepository expenseRepository;

    public DeleteExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }   

    public void delete(String expenseId, String userId) {
        ExpenseEntity expense = expenseRepository.findId(expenseId)
                .orElseThrow(() -> new BusinessException("Despesa nao encontrada", HttpStatus.NOT_FOUND));

        if (!expense.getEvent().getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to delete this expense", HttpStatus.FORBIDDEN);
        }
        expenseRepository.deleteById(expenseId);

    }

}
