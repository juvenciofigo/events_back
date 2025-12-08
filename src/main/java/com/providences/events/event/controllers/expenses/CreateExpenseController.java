package com.providences.events.event.controllers.expenses;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.ExpenseDTO;
import com.providences.events.event.services.expense.CreateExpenseService;
import com.providences.events.event.services.expense.UpdateExpenseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/expenses")
public class CreateExpenseController {
    private CreateExpenseService expenseRepository;
    private UpdateExpenseService updateExpenseService;

    public CreateExpenseController(CreateExpenseService expenseRepository, UpdateExpenseService updateExpenseService) {
        this.expenseRepository = expenseRepository;
        this.updateExpenseService = updateExpenseService;
    }

    @PostMapping("event/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ExpenseDTO.Response> postMethodName(
            @RequestBody ExpenseDTO.Create entity,
            @PathVariable String eventId,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseRepository.createExpense(entity, eventId, userData.getUserId()));
    }

    @PutMapping("{expenseId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ExpenseDTO.Response> updateExpense(
            @PathVariable(required = true, value = "expenseId") String expenseId,
            @RequestBody ExpenseDTO.Create entity,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        updateExpenseService.update(expenseId, userData.getUserId(), entity);

        return ResponseEntity.ok().build();
    }

}
