package com.providences.events.plans.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.plans.dto.SubscriptionDTO;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.entities.SubscriptionEntity.PayerType;
import com.providences.events.plans.repositories.SubscriptionRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional
public class FetchSubscriptionsByPayerService {
    private SubscriptionRepository subscriptionRepository;
    private OrganizerRepository organizerRepository;
    private SupplierRepository supplierRepository;

    public FetchSubscriptionsByPayerService(SubscriptionRepository subscriptionRepository,
            OrganizerRepository organizerRepository,
            SupplierRepository supplierRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.organizerRepository = organizerRepository;
        this.supplierRepository = supplierRepository;
    }

    public Set<SubscriptionDTO.Response> execute(String payerType, String userId, String payerId) {
        PayerType payer;
        try {
            payer = PayerType.valueOf(payerType.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de pagador inválido", HttpStatus.BAD_REQUEST);
        }

        Set<SubscriptionEntity> subscription = switch (payer) {
            case ORGANIZER -> {
                OrganizerEntity organizer = organizerRepository.findById(payerId)
                        .orElseThrow(() -> new BusinessException("Organizador não encontrado", HttpStatus.NOT_FOUND));
                if (!organizer.getUser().getId().equals(userId)) {
                    throw new BusinessException("Usuário não possui permissão para visualizar as assinaturas",
                            HttpStatus.FORBIDDEN);
                }
                yield subscriptionRepository.findByPayerTypeAndOrganizer(payerId, payer);
            }
            case SUPPLIER -> {
                SupplierEntity supplier = supplierRepository.findById(payerId)
                        .orElseThrow(() -> new BusinessException("Fornecedor não encontrado", HttpStatus.NOT_FOUND));
                if (!supplier.getUser().getId().equals(userId)) {
                    throw new BusinessException("Usuário não possui permissão para visualizar as assinaturas",
                            HttpStatus.FORBIDDEN);
                }
                yield subscriptionRepository.findByPayerTypeAndSupplier(payerId, payer);
            }
            default -> throw new BusinessException("Tipo de pagador inválido", HttpStatus.BAD_REQUEST);
        };

        if (subscription == null) {
            throw new BusinessException("Nenhuma assinatura encontrada para o usuário", HttpStatus.NOT_FOUND);
        }

        return subscription.stream().map(SubscriptionDTO.Response::response).collect(Collectors.toSet());
    }
}
