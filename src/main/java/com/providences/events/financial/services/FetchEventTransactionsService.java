package com.providences.events.financial.services;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.financial.dto.financial;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.payment.repository.PaymentRepository;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
public class FetchEventTransactionsService {

    private final PaymentRepository paymentRepository;
    private final EventRepository eventRepository;

    public FetchEventTransactionsService(PaymentRepository paymentRepository, EventRepository eventRepository) {
        this.paymentRepository = paymentRepository;
        this.eventRepository = eventRepository;
    }

    public SystemDTO.ItemWithPage<financial.Transaction> execute(String eventId, String userId, int page,
            int limit) {
        EventEntity event = eventRepository.findId(eventId)
                .orElseThrow(() -> new BusinessException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("User is not authorized to access this event", HttpStatus.UNAUTHORIZED);
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<PaymentEntity> paymentsPage = paymentRepository.findTransactionsByEventId(eventId, pageable);

        var list = paymentsPage.getContent().stream()
                .map(p -> new financial.Transaction(
                        p.getId(),
                        p.getCreatedAt(),
                        p.getPayerGuest() != null ? p.getPayerGuest().getName()
                                : (p.getPayer() != null ? p.getPayer() : "Unknown"),
                        p.getPayerGuest() != null ? p.getPayerGuest().getId()
                                : (p.getPayer() != null ? p.getPayer() : "Unknown"),
                        p.getTargetSeat() != null ? p.getTargetSeat().getName() : "General",
                        p.getAmount() != null ? p.getAmount().doubleValue() : 0.0,
                        p.getPaymentMethod() != null ? p.getPaymentMethod().toString() : "Unknown",
                        p.getStatus() != null ? p.getStatus().toString() : "Unknown"))
                .collect(Collectors.toList());

        return new SystemDTO.ItemWithPage<>(list, page, paymentsPage.getTotalPages(),
                paymentsPage.getTotalElements());
    }
}