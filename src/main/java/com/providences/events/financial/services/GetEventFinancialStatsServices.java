package com.providences.events.financial.services;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.ExpenseEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.ExpenseRepository;
import com.providences.events.financial.dto.financial;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.payment.repository.PaymentRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class GetEventFinancialStatsServices {
        private EventRepository eventRepository;
        private PaymentRepository paymentRepository;
        private ExpenseRepository expenseRepository;

        public GetEventFinancialStatsServices(EventRepository eventRepository, PaymentRepository paymentRepository,
                        ExpenseRepository expenseRepository) {
                this.eventRepository = eventRepository;
                this.paymentRepository = paymentRepository;
                this.expenseRepository = expenseRepository;
        }

        public financial.Response execute(String eventId, String userId) {
                EventEntity event = eventRepository.findId(eventId)
                                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

                if (!event.getOrganizer().getUser().getId().equals(userId)) {
                        throw new BusinessException("User is not authorized to access this event",
                                        HttpStatus.UNAUTHORIZED);
                }

                Set<PaymentEntity> payments = paymentRepository.findByEventId(eventId);
                Set<ExpenseEntity> expenses = expenseRepository.findByEventId(eventId);

                double totalRevenue = payments.stream()
                                .filter(p -> p.getAmount() != null)
                                .mapToDouble(p -> p.getAmount().doubleValue())
                                .sum();

                double totalExpenses = expenses.stream()
                                .filter(e -> e.getAmount() != null)
                                .mapToDouble(e -> e.getAmount().doubleValue())
                                .sum();

                double netRevenue = totalRevenue - totalExpenses;

                financial.Response response = new financial.Response();
                response.totalRevenue = totalRevenue;
                response.totalExpenses = totalExpenses;
                response.netRevenue = netRevenue;

                // Expenses by Category
                response.expensesByCategory = expenses.stream()
                                .filter(e -> e.getAmount() != null)
                                .collect(Collectors.groupingBy(
                                                e -> e.getCategory() != null ? e.getCategory() : "UnCategorized",
                                                Collectors.summingDouble(e -> e.getAmount().doubleValue())));

                // Expenses by Status
                response.expensesByStatus = expenses.stream()
                                .filter(e -> e.getAmount() != null)
                                .collect(java.util.stream.Collectors.groupingBy(
                                                e -> e.getPaymentStatus() != null ? e.getPaymentStatus().toString()
                                                                : "Unknown",
                                                java.util.stream.Collectors
                                                                .summingDouble(e -> e.getAmount().doubleValue())));

                // Net Profit Margin
                response.netProfitMargin = totalRevenue > 0 ? (netRevenue / totalRevenue) * 100 : 0.0;

                // Revenue by Seat
                response.revenueBySeat = payments.stream()
                                .filter(p -> p.getTargetSeat() != null)
                                .collect(Collectors.groupingBy(
                                                p -> p.getTargetSeat().getName(),
                                                Collectors.toList()))
                                .entrySet().stream()
                                .map(entry -> {
                                        String seatName = entry.getKey();
                                        List<PaymentEntity> seatPayments = entry.getValue();
                                        double revenue = seatPayments.stream()
                                                        .filter(p -> p.getAmount() != null)
                                                        .mapToDouble(p -> p.getAmount().doubleValue())
                                                        .sum();
                                        int quantity = seatPayments.size();
                                        return new financial.RevenueBySeat(seatName, revenue, quantity);
                                })
                                .collect(Collectors.toList());

                // Revenue by Payment Method
                response.revenueByPaymentMethod = payments.stream()
                                .filter(p -> p.getAmount() != null)
                                .map(p -> {
                                        String paymentMethod = p.getPaymentMethod() != null
                                                        ? p.getPaymentMethod().toString()
                                                        : "Unknown";
                                        double revenue = p.getAmount() != null ? p.getAmount().doubleValue() : 0.0;
                                        int quantity = 1;
                                        return new financial.RevenueByPaymentMethod(paymentMethod, revenue, quantity);
                                })
                                .collect(Collectors.toList());

                // Recent Transactions
                response.recentTransactions = payments.stream()
                                .sorted(Comparator.comparing(PaymentEntity::getCreatedAt).reversed())
                                .limit(10)
                                .map(p -> new financial.Transaction(
                                                p.getId(),
                                                p.getCreatedAt(),
                                                p.getPayerGuest() != null ? p.getPayerGuest().getName()
                                                                : (p.getPayer() != null ? p.getPayer() : "Unknown"),
                                                p.getPayerGuest() != null ? p.getPayerGuest().getId()
                                                                : (p.getPayer() != null ? p.getPayer() : "Unknown"),
                                                p.getTargetSeat() != null ? p.getTargetSeat().getName() : "General",
                                                p.getAmount() != null ? p.getAmount().doubleValue() : 0.0,
                                                p.getPaymentMethod() != null ? p.getPaymentMethod().toString()
                                                                : "Unknown",
                                                p.getStatus() != null ? p.getStatus().toString() : "Unknown"))
                                .collect(Collectors.toList());

                response.totalFees = 0.0;
                response.totalDiscounts = 0.0;

                return response;
        }
}
