package com.providences.events.event.controllers.expenses;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.ExpenseDTO;
import com.providences.events.event.services.expense.FetchExpensesService;
import com.providences.events.shared.dto.SystemDTO;

@RestController
@RequestMapping("/expenses")
public class GetExpensesController {
    private FetchExpensesService fetchExpensesService;

    public GetExpensesController(FetchExpensesService fetchExpensesService) {
        this.fetchExpensesService = fetchExpensesService;
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public SystemDTO.ItemWithPage<ExpenseDTO.Response> fetchExpenses(
            @PathVariable String eventId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return fetchExpensesService.fetchExpenses(eventId, userData.getUserId(), pageNumber, limit, sort);
    }
}
