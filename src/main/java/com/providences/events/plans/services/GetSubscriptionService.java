package com.providences.events.plans.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.providences.events.plans.dto.SubscriptionDTO;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.plans.repositories.SubscriptionRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetSubscriptionService {
    private SubscriptionRepository subscriptionRepository;

    public GetSubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionDTO.Response execute(String subscriptionId, String userId) {
        SubscriptionEntity subscription = subscriptionRepository.findId(subscriptionId)
                .orElseThrow(() -> new BusinessException("Subscrição não encontrada", HttpStatus.NOT_FOUND));

        if (subscription.getSupplier() != null) {
            if (!subscription.getSupplier().getUser().getId().equals(userId)) {
                throw new BusinessException("Sem autorização ", HttpStatus.UNAUTHORIZED);
            }
        }
        if (subscription.getOrganizer() != null) {
            if (!subscription.getOrganizer().getUser().getId().equals(userId)) {
                throw new BusinessException("Sem autorização ", HttpStatus.UNAUTHORIZED);
            }
        }

        return SubscriptionDTO.Response.response(subscription);

    }
}
