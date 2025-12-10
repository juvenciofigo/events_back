package com.providences.events.event.controllers.expenses;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.services.expense.DeleteExpenseService;

@RestController
@RequestMapping("/expenses")
public class DeleteExpenseController {
    private DeleteExpenseService deleteExpenseService;

    public DeleteExpenseController(DeleteExpenseService deleteExpenseService) {
        this.deleteExpenseService = deleteExpenseService;
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> delete(@PathVariable String expenseId, Authentication authentication) {
        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        deleteExpenseService.delete(expenseId, userData.getUserId());
        return ResponseEntity.ok().build();
    }
}
