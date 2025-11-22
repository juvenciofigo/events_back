package com.providences.events.plans.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.plans.dto.PlanDTO;
import com.providences.events.plans.entities.PlanEntity;
import com.providences.events.plans.repositories.PlanRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class UpdatePlanService {
    private PlanRepository planRepository;

    public UpdatePlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanDTO.Response updatePlan(String planId, PlanDTO.Create plan) {
        PlanEntity planToUpdate = planRepository.findById(planId)
                .orElseThrow(() -> new BusinessException("Plano não encontrado", HttpStatus.NOT_FOUND));
        planToUpdate.setName(plan.getName());
        planToUpdate.setDescription(plan.getDescription());
        planToUpdate.setPriceMonthly(plan.getPriceMonthly());
        planToUpdate.setPriceYearly(plan.getPriceYearly());
        planToUpdate.setResources(plan.getResources());
        planToUpdate.setLevel(plan.getLevel());
        planToUpdate.setPlanType(PlanEntity.PlanType.valueOf(plan.getPlanType()));
        planToUpdate.setFeatures(plan.getFeatures());
        return PlanDTO.Response.response(planRepository.save(planToUpdate));
    }
}
