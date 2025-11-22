package com.providences.events.plans.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.plans.dto.PlanDTO;
import com.providences.events.plans.entities.PlanEntity.PlanType;
import com.providences.events.plans.repositories.PlanRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchPlansService {
    private PlanRepository planRepository;

    public FetchPlansService(
            PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Set<PlanDTO.Response> execute(String planType) {

        // Validar o plano
        PlanType type;
        try {
            type = PlanType.valueOf(planType.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de plano inválido", HttpStatus.BAD_REQUEST);
        }

        return planRepository.findAllByPlanType(type).stream()
                .map(PlanDTO.Response::response)
                .collect(Collectors.toSet());

    }
}
