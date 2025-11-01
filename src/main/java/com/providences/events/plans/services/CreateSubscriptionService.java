package com.providences.events.plans.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.payment.PaymentEntity;
import com.providences.events.payment.PaymentEntity.ReceiverType;
import com.providences.events.payment.PaymentEntity.Target;
import com.providences.events.payment.dto.CreatePaymentDTO;
import com.providences.events.payment.services.CreatePaymentService;
import com.providences.events.plans.dto.CreateSubscriptionDTO;
import com.providences.events.plans.entities.AddonPlanEntity;
import com.providences.events.plans.entities.OrganizerPlanEntity;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.entities.SupplierPlanEntity;
import com.providences.events.plans.entities.SubscriptionEntity.PayerType;
import com.providences.events.plans.entities.SubscriptionEntity.PlanType;
import com.providences.events.plans.repositories.AddonPlanRepository;
import com.providences.events.plans.repositories.OrganizerPlanRepository;
import com.providences.events.plans.repositories.SubscriptionRepository;
import com.providences.events.plans.repositories.SupplierPlanRepository;
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
    private final SupplierPlanRepository supplierPlanRepository;
    private final OrganizerPlanRepository organizerPlanRepository;
    private final AddonPlanRepository addonPlanRepository;

    // payment
    private CreatePaymentService createPaymentService;

    public CreateSubscriptionService(SubscriptionRepository subscriptionRepository,
            OrganizerRepository organizerRepository, SupplierRepository supplierRepository,
            SupplierPlanRepository supplierPlanRepository, OrganizerPlanRepository organizerPlanRepository,
            AddonPlanRepository addonPlanRepository, CreatePaymentService createPaymentService) {
        this.subscriptionRepository = subscriptionRepository;
        this.organizerRepository = organizerRepository;
        this.supplierRepository = supplierRepository;
        this.supplierPlanRepository = supplierPlanRepository;
        this.organizerPlanRepository = organizerPlanRepository;
        this.addonPlanRepository = addonPlanRepository;
        this.createPaymentService = createPaymentService;
    }

    public SubscriptionEntity execute(CreateSubscriptionDTO.Request data) {

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusDays(30));
        subscription.setAutoRenew(false);
        subscription.setPlanType(PlanType.valueOf(data.getPlanType()));

        CreatePaymentDTO.Request paymentData = new CreatePaymentDTO.Request();
        paymentData.setReceiverType(ReceiverType.PLATFORM);
        paymentData.setReceiverPlatform(true);
        paymentData.setTarget(Target.SUBSCRIPTION);
        paymentData.setPaymentMethod(data.getPaymentMethod());

        // Begin Who will pay

        if (data.getPayerType().equals(PayerType.ORGANIZER.toString())) {
            OrganizerEntity organizer = organizerRepository.findById(data.getOrganizerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organizador nao encontrado!"));

            paymentData
                    .setPayerType(com.providences.events.payment.PaymentEntity.PayerType.ORGANIZER);
            paymentData.setPayerOrganizer(organizer);
            subscription.setPayerType(PayerType.ORGANIZER);
            subscription.setOrganizer(organizer);
        }

        if (data.getPayerType().equals(PayerType.SUPPLIER.name())) {
            SupplierEntity supplier = supplierRepository.findById(data.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado!"));

            paymentData.setPayerType(com.providences.events.payment.PaymentEntity.PayerType.SUPPLIER);
            paymentData.setPayerSupplier(supplier);
            subscription.setPayerType(PayerType.SUPPLIER);
            subscription.setSupplier(supplier);
        }

        ////////// End Who will pay

        ///////// Begin which ones plan pay

        if (data.getPlanType().equals(PlanType.ADDON.name())) {
            AddonPlanEntity addonPlan = addonPlanRepository.findById(data.getAddonPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado!"));

            paymentData.setAmount(new BigDecimal(addonPlan.getPriceMonthly()));
            subscription.setAddonPlan(addonPlan);
        }

        if (data.getPlanType().equals(PlanType.ORGANIZER.name())) {
            System.out.println("ddddddddddddddddd" + data.getOrganizerPlanId());
            List<OrganizerPlanEntity> todos = organizerPlanRepository.findAll();

            System.out.println("ddddddddddddddddd " + todos.stream()
                    .map(OrganizerPlanEntity::getId) // pega o nome de cada entidade
                    .toList());
            OrganizerPlanEntity organizerPlan = organizerPlanRepository
                    .fetchById("7c7cf4b1-0cd0-4b9e-ae26-22f0c96be8f5")
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado!"));

            paymentData.setAmount(new BigDecimal(organizerPlan.getPriceMonthly()));
            subscription.setOrganizerPlan(organizerPlan);
        }

        if (data.getPlanType().equals(PlanType.SUPPLIER.name())) {
            SupplierPlanEntity supplierPlan = supplierPlanRepository.findById(data.getSupplierPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado!"));

            paymentData.setAmount(new BigDecimal(supplierPlan.getPriceMonthly()));
            subscription.setSupplierPlan(supplierPlan);
        }

        //////// End which ones plan pay

        // fazer o pagamento por ultimo
        paymentData.setSubscription(subscription);

        PaymentEntity paymentResponse = createPaymentService.execute(paymentData);

        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);

        System.out.println(paymentResponse);
        System.out.println(savedSubscription);

        return savedSubscription;

    }
}
