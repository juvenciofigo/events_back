package com.providences.events.plans.dto;

import java.time.LocalDateTime;

import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.plans.entities.SubscriptionEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SubscriptionDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {

        public String paymentMethod;

        @NotBlank(message = "Informe numemero para cobrar o pagamento!")
        public String payerNum;

        // plano a ser pago
        ///////////// Begin which ones plan pay

        @NotBlank(message = "Indique o tipo de plano")
        public String planType;

        @NotBlank(message = "Indique o plano")
        public String planId;

        ////////// End which ones plan pay

        @NotBlank(message = "Indique o tipo o pagador")
        public String payerType;

        public String payerId;

        private String billingCycle; // MONTHLY / YEARLY

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        public String id;
        public String status;
        public String planType;
        public Boolean autoRenew;
        public LocalDateTime startDate;
        public LocalDateTime endDate;
        public String payerType;
        public String payerId;
        private String billingCycle;
        public PaymentDTO.Response payment;

        public static Response response(SubscriptionEntity subscription) {
            return new Response(
                    subscription.getId(),
                    subscription.getStatus().name(),
                    subscription.getPlanType().name(),
                    subscription.getAutoRenew(),
                    subscription.getStartDate(),
                    subscription.getEndDate(),
                    subscription.getPayerType().name(),
                    subscription.getOrganizer() != null ? subscription.getOrganizer().getId()
                            : subscription.getSupplier() != null ? subscription.getSupplier().getId() : null,
                    subscription.getBillingCycle() != null ? subscription.getBillingCycle().toString() : null,
                    PaymentDTO.Response.response(subscription.getPayment()));
        }

    }

}
