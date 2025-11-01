package com.providences.events.plans.dto;

import java.time.LocalDateTime;

import com.providences.events.payment.dto.CreatePaymentDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CreateSubscriptionDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        private String paymentMethod;

        // plano a ser pago
        ///////////// Begin which ones plan pay

        @NotBlank(message = "Indique o tipo de plano")
        private String planType;

        private String supplierPlanId;

        private String addonPlanId;

        private String organizerPlanId;

        ////////// End which ones plan pay

        @NotBlank(message = "Indique o tipo o pagador")
        private String payerType;

        // `relacionamento como provedor de serviços,
        private String supplierId;

        // ralacionamento com organizador
        private String organizerId;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String status;
        private String planType;
        private Boolean autoRenew;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String payerType;
        private String payerId;
        private CreatePaymentDTO.Response payment;

    }

}
