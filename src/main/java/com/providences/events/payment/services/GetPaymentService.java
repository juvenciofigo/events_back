package com.providences.events.payment.services;

import org.springframework.http.HttpStatus;

import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.payment.repository.PaymentRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetPaymentService {
    private PaymentRepository paymentRepository;

    public GetPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    };

    public PaymentDTO.Response execute(String paymentId, String userId) {
        PaymentEntity payment = paymentRepository.getId(paymentId)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado", HttpStatus.NOT_FOUND));

        if (!payment.getPayerId().equals(userId)) {
            throw new BusinessException("Sem autorização!", HttpStatus.FORBIDDEN);
        }

        return PaymentDTO.Response.response(payment);

    }
}
