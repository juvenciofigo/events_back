package com.providences.events.plans.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.entities.PaymentEntity;
import com.providences.events.payment.entities.PaymentEntity.ReceiverType;
import com.providences.events.payment.entities.PaymentEntity.Target;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.plans.dto.SubscriptionDTO;
import com.providences.events.plans.entities.PlanEntity;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.entities.PlanEntity.PlanType;
import com.providences.events.plans.entities.SubscriptionEntity.BillingCycle;
import com.providences.events.plans.entities.SubscriptionEntity.PayerType;
import com.providences.events.plans.repositories.PlanRepository;
import com.providences.events.plans.repositories.SubscriptionRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional
public class CreateSubscriptionService {

        private final SubscriptionRepository subscriptionRepository;

        // payer
        private final OrganizerRepository organizerRepository;
        private final SupplierRepository supplierRepository;

        // paid plan
        private final PlanRepository planRepository;

        // payment
        private CreatePaymentService createPaymentService;

        public CreateSubscriptionService(SubscriptionRepository subscriptionRepository,
                        OrganizerRepository organizerRepository,
                        SupplierRepository supplierRepository,
                        PlanRepository planRepository,
                        CreatePaymentService createPaymentService) {
                this.subscriptionRepository = subscriptionRepository;
                this.organizerRepository = organizerRepository;
                this.supplierRepository = supplierRepository;
                this.planRepository = planRepository;
                this.createPaymentService = createPaymentService;
        }

        public SubscriptionDTO.Response execute(SubscriptionDTO.Create data) {

                // Validar o plano
                PlanType planType;
                try {
                        planType = PlanType.valueOf(data.getPlanType().toUpperCase());
                } catch (Exception e) {
                        throw new BusinessException("Tipo de plano inválido", HttpStatus.BAD_REQUEST);
                }

                PayerType payerType;
                try {
                        payerType = PayerType.valueOf(data.getPayerType().toUpperCase());
                } catch (Exception e) {
                        throw new BusinessException("Tipo de pagador inválido", HttpStatus.BAD_REQUEST);
                }

                BillingCycle billingCycle;
                try {
                        billingCycle = data.getBillingCycle() != null
                                        ? BillingCycle.valueOf(data.getBillingCycle().toUpperCase())
                                        : null;
                } catch (Exception e) {
                        throw new BusinessException("Tipo de ciclo de pagamento inválido", HttpStatus.BAD_REQUEST);
                }

                // Criar a subscricao
                SubscriptionEntity subscription = new SubscriptionEntity();
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(LocalDateTime.now().plusMonths(1));
                subscription.setAutoRenew(false);
                subscription.setPlanType(planType);
                subscription.setPayerType(payerType);
                subscription.setBillingCycle(billingCycle);

                // criar pagamento
                PaymentDTO.Request paymentData = new PaymentDTO.Request();
                paymentData.setReceiverType(ReceiverType.PLATFORM);
                paymentData.setReceiverPlatform(true);
                paymentData.setTarget(Target.SUBSCRIPTION);
                paymentData.setPaymentMethod(data.getPaymentMethod());
                paymentData.setPayerNum(data.getPayerNum());

                // Begin Who will pay

                switch (payerType) {
                        case ORGANIZER -> {
                                OrganizerEntity org = organizerRepository.findId(data.getPayerId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Organizador não encontrado!"));

                                paymentData.setPayerType(
                                                com.providences.events.payment.entities.PaymentEntity.PayerType.ORGANIZER);
                                paymentData.setPayerOrganizer(org);

                                subscription.setOrganizer(org);
                        }

                        case SUPPLIER -> {
                                SupplierEntity sup = supplierRepository.findId(data.getPayerId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Fornecedor não encontrado!"));

                                paymentData.setPayerType(
                                                com.providences.events.payment.entities.PaymentEntity.PayerType.SUPPLIER);
                                paymentData.setPayerSupplier(sup);

                                subscription.setSupplier(sup);
                        }
                }

                ////////// End Who will pay

                ///////// Begin which ones plan pay

                PlanEntity plan = planRepository
                                .getByIdAndType(data.getPlanId(), planType)
                                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado!"));

                paymentData.setAmount(BigDecimal.valueOf(plan.getPriceMonthly()));
                subscription.setPlan(plan);

                //////// End which ones plan pay

                // fazer o pagamento por ultimo
                paymentData.setSubscription(subscription);

                PaymentEntity paymentResponse = createPaymentService.execute(paymentData);

                subscription.setPayment(paymentResponse);
                SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);

                SubscriptionDTO.Response responseDTO = SubscriptionDTO.Response.response(savedSubscription);

                return responseDTO;

        }
}
