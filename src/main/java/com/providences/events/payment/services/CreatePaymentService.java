package com.providences.events.payment.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fc.sdk.APIResponse;
import com.providences.events.config.MpesaService;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.payment.PaymentRepository;
import com.providences.events.payment.PaymentEntity.PaymentMethod;
import com.providences.events.payment.PaymentReferenceEntity;
import com.providences.events.payment.dto.CreatePaymentDTO;
import com.providences.events.shared.exception.exceptions.MpesaPaymentException;

import jakarta.validation.Valid;

@Service
@Transactional
@Validated
public class CreatePaymentService {
    private final PaymentRepository paymentRepository;

    // Mpesa
    private final MpesaService mpesaService;

    //
    private final PaymentReferenceRepository paymentReferenceRepository;

    public CreatePaymentService(PaymentRepository paymentRepository, MpesaService mpesaService,
            PaymentReferenceRepository paymentReferenceRepository) {
        this.paymentRepository = paymentRepository;
        this.mpesaService = mpesaService;
        this.paymentReferenceRepository = paymentReferenceRepository;
    }

    private static String generateThirdPartyReference() {
        String uuid = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "");
        String ref = "REF" + uuid.substring(0, 8);
        return ref.toUpperCase();
    }

    private static String generateTransactionReference() {
        String uuid = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "");
        String ref = "TXN" + uuid.substring(0, 13);
        return ref.toUpperCase();
    }

    public CreatePaymentDTO.Response execute(@Valid CreatePaymentDTO.Request data) {

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
        payment.setSeat(data.getSeat());

        String transactionRef = generateTransactionReference();
        String thirdPartyRef = generateThirdPartyReference();

        APIResponse responseMpesa = mpesaService.executePayment(transactionRef, thirdPartyRef, data.payerNum,
                payment.getAmount());

        if (responseMpesa == null) {
            throw new MpesaPaymentException(400);
        }

        if (responseMpesa.getStatusCode() != 200 && responseMpesa.getStatusCode() != 201) {
            throw new MpesaPaymentException(responseMpesa.getStatusCode());

        }

        PaymentEntity createdPayment = paymentRepository.save(payment);

        // Armazenar referencia do pagamento
        PaymentReferenceEntity reference = new PaymentReferenceEntity();
        reference.setTransactionReference(transactionRef);
        reference.setThirdPartyReference(thirdPartyRef);
        reference.setGatewayResponse(responseMpesa.getResult());
        reference.setPayment(payment);
        paymentReferenceRepository.save(reference);

        return new CreatePaymentDTO.Response(
                createdPayment.getId(),
                createdPayment.getStatus().name(),
                createdPayment.getAmount(),
                createdPayment.getCurrency().name(),
                createdPayment.getPaymentMethod().name(),
                createdPayment.getCreatedAt());
    }
}