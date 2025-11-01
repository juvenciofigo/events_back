package com.providences.events.payment.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.payment.PaymentEntity;
import com.providences.events.payment.PaymentRepository;
import com.providences.events.payment.PaymentEntity.PaymentMethod;
import com.providences.events.payment.dto.CreatePaymentDTO;

@Service
@Transactional
public class CreatePaymentService {
    private final PaymentRepository paymentRepository;

    public CreatePaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentEntity execute(CreatePaymentDTO.Request data) {

        PaymentEntity payment = new PaymentEntity();

        payment.setPaymentMethod(PaymentMethod.valueOf(data.getPaymentMethod().toUpperCase()));
        payment.setAmount(data.getAmount());
        payment.setDescription(data.getDescription());
        payment.setCurrency(data.getCurrency());

        payment.setPayerType(data.getPayerType());
        payment.setPayerSupplier(data.getPayerSupplier());
        payment.setPayerOrganizer(data.getPayerOrganizer());
        payment.setPayerGuest(data.getPayerGuest());

        payment.setReceiverType(data.getReceiverType());
        payment.setReceiverSupplier(data.getReceiverSupplier());
        payment.setReceiverOrganizer(data.getReceiverOrganizer());
        payment.setReceiverPlatform(data.getReceiverPlatform());

        payment.setTarget(data.getTarget());
        payment.setService(data.getService());
        payment.setSubscription(data.getSubscription());
        payment.setTicket(data.getTicket());

        return paymentRepository.save(payment);

    }
}
