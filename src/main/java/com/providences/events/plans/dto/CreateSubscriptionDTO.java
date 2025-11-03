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

        public String paymentMethod;
        @NotBlank(message = "Informe numemero para cobrar o pagamento!")
        public String customerNum;

        // plano a ser pago
        ///////////// Begin which ones plan pay

        @NotBlank(message = "Indique o tipo de plano")
        public String planType;

        public String supplierPlanId;

        public String addonPlanId;

        public String organizerPlanId;

        ////////// End which ones plan pay

        @NotBlank(message = "Indique o tipo o pagador")
        public String payerType;

        // `relacionamento como provedor de serviços,
        public String supplierId;

        // ralacionamento com organizador
        public String organizerId;

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
        public CreatePaymentDTO.Response payment;

    }

}
