package com.providences.events.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.providences.events.guest.GuestEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.payment.PaymentEntity.Currency;
import com.providences.events.payment.PaymentEntity.PayerType;
import com.providences.events.payment.PaymentEntity.ReceiverType;
import com.providences.events.payment.PaymentEntity.Target;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.ticket.entities.TicketEntity;

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
        public String paymentMethod;

        @NotBlank(message = "Informe numemero para cobrar o pagamento!")
        public String payerNum;

        @NotBlank(message = "Informe o valor de pagamento!")
        @Positive(message = "O valor do pagamento precisa ser maior que zero!")
        public BigDecimal amount;

        public String description;

        public Currency currency;

        // Payer
        @NotBlank(message = "Infome quem faz o pagamento!")
        public PayerType payerType;

        public SupplierEntity payerSupplier;

        public OrganizerEntity payerOrganizer;

        public GuestEntity payerGuest;

        // Receiver

        @NotBlank(message = "Infome quem para quem vai o pagamento!")
        public ReceiverType receiverType;

        public SupplierEntity receiverSupplier;

        public OrganizerEntity receiverOrganizer;

        public Boolean receiverPlatform;

        // Target
        @NotBlank(message = "Informe quem recebe o pagamento")
        public Target target;

        public ServiceEntity service;

        public SubscriptionEntity subscription;

        public TicketEntity ticket;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        public String id;
        public String status;

        public BigDecimal amount;
        public String currency;
        public String paymentMethod;
        public LocalDateTime createdAt;

    }
}
