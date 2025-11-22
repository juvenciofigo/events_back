package com.providences.events.plans.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.entities.PaymentEntity.ReceiverType;
import com.providences.events.payment.entities.PaymentEntity.Target;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.plans.entities.PlanEntity;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.entities.SubscriptionEntity.BillingCycle;
import com.providences.events.plans.entities.SubscriptionEntity.PayerType;
import com.providences.events.plans.repositories.SubscriptionRepository;

@Service
public class AutoRenewalService {

    private SubscriptionRepository subscriptionRepository;
    private CreatePaymentService createPaymentService;

    @Scheduled(cron = "0 0 3 * * *") // todos os dias às 03:00
    public void processAutoRenewals() {
        LocalDate today = LocalDate.now();

        // Buscar quem vence hoje e tem autoRenew = true
        Set<SubscriptionEntity> expiring = subscriptionRepository.findAllByEndDateAndAutoRenew(today.atStartOfDay(),
                true);

        for (SubscriptionEntity sub : expiring) {

            PlanEntity plan = sub.getPlan();

            PaymentDTO.Request paymentData = new PaymentDTO.Request();
            paymentData.setReceiverType(ReceiverType.PLATFORM);
            paymentData.setReceiverPlatform(true);
            paymentData.setTarget(Target.SUBSCRIPTION);
            paymentData.setSubscription(sub);

            // Determinar valor baseado no ciclo
            if (sub.getBillingCycle() == BillingCycle.MONTHLY) {
                paymentData.setAmount(BigDecimal.valueOf(plan.getPriceMonthly()));
                sub.setEndDate(sub.getEndDate().plusMonths(1));
            } else {
                paymentData.setAmount(BigDecimal.valueOf(plan.getPriceYearly()));
                sub.setEndDate(sub.getEndDate().plusYears(1));
            }

            // Definir pagador
            if (sub.getPayerType() == PayerType.ORGANIZER) {
                paymentData.setPayerOrganizer(sub.getOrganizer());
            } else {
                paymentData.setPayerSupplier(sub.getSupplier());
            }

            // Processar pagamento
            createPaymentService.execute(paymentData);

            // Atualizar assinatura
            subscriptionRepository.save(sub);
        }
    }
}
