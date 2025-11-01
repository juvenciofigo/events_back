package com.providences.events.payment.dto;

import java.math.BigDecimal;
import com.providences.events.guest.GuestEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.payment.PaymentEntity.Currency;
import com.providences.events.payment.PaymentEntity.PayerType;
import com.providences.events.payment.PaymentEntity.ReceiverType;
import com.providences.events.payment.PaymentEntity.Target;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.ticket.TicketEntity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CreatePaymentDTO {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Request {
        @NotBlank(message = "Informe o metodo de pagamento!")
        private String paymentMethod;

        @Column(precision = 10, scale = 2, nullable = false)
        @NotBlank(message = "Informe o valor de pagamento!")
        @Positive(message = "O valor do pagamento precisa ser maior que zero!")
        private BigDecimal amount;

        private String description;

        private Currency currency;

        // Payer
        @NotBlank(message = "Infome quem faz o pagamento!")
        private PayerType payerType;

        private SupplierEntity payerSupplier;

        private OrganizerEntity payerOrganizer;

        private GuestEntity payerGuest;

        // Receiver

        @NotBlank(message = "Infome quem para quem vai o pagamento!")
        private ReceiverType receiverType;

        private SupplierEntity receiverSupplier;

        private OrganizerEntity receiverOrganizer;

        private Boolean receiverPlatform;

        // Target
        @NotBlank(message = "Informe quem recebe o pagamento")
        private Target target;

        private ServiceEntity service;

        private SubscriptionEntity subscription;

        private TicketEntity ticket;

    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private String id;
    }
}
