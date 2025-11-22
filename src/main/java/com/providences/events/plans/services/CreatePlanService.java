package com.providences.events.plans.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.plans.dto.PlanDTO;
import com.providences.events.plans.entities.PlanEntity;
import com.providences.events.plans.entities.PlanEntity.PlanType;
import com.providences.events.plans.repositories.PlanRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class CreatePlanService {

    private PlanRepository planRepository;

    public CreatePlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanDTO.Response execute(PlanDTO.Create data) {

        PlanType planType;

        try {
            planType = PlanType.valueOf(data.getPlanType().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de plano inválido", HttpStatus.BAD_REQUEST);
        }

        PlanEntity plan = new PlanEntity();
        plan.setName(data.getName());
        plan.setDescription(data.getDescription());
        plan.setResources(data.getResources());
        plan.setPriceMonthly(data.getPriceMonthly());
        plan.setPriceYearly(data.getPriceYearly());
        plan.setFeatures(data.getFeatures());
        plan.setLevel(data.getLevel());
        plan.setPlanType(planType);

        return PlanDTO.Response.response(planRepository.save(plan));
    }

}
