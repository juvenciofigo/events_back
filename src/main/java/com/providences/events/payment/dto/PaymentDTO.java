package com.providences.events.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.providences.events.event.entities.SeatEntity;
import com.providences.events.guest.GuestEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.payment.entities.PaymentEntity.*;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier.SupplierEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PaymentDTO {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Request {
        @NotBlank(message = "Informe o metodo de pagamento!")
        public String paymentMethod;

        @NotBlank(message = "Informe numero para cobrar o pagamento!")
        @Size(max = 9, min = 9, message = "O nomero deve conter 9 digitos")
        public String payerNum;

        @NotNull(message = "Informe o valor de pagamento!")
        @Positive(message = "O valor do pagamento precisa ser maior que zero!")
        public BigDecimal amount;

        public String description;

        public Currency currency;

        // Payer
        @NotNull(message = "Infome quem faz o pagamento!")
        public PayerType payerType;

        public SupplierEntity payerSupplier;

        public OrganizerEntity payerOrganizer;

        public GuestEntity payerGuest;

        // Receiver

        @NotNull(message = "Infome quem para quem vai o pagamento!")
        public ReceiverType receiverType;

        public SupplierEntity receiverSupplier;

        public OrganizerEntity receiverOrganizer;

        public Boolean receiverPlatform;

        // Target
        @NotNull(message = "Informe quem recebe o pagamento")
        public Target target;

        public ServiceEntity service;

        public SubscriptionEntity subscription;

        public SeatEntity seat;

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

        public static Response response(PaymentEntity payment) {
            return new Response(
                    payment.getId(),
                    payment.getStatus().name(),
                    payment.getAmount(),
                    payment.getCurrency().name(),
                    payment.getPaymentMethod().name(),
                    payment.getCreatedAt());
        }
    }
}
