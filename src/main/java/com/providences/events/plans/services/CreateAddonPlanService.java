package com.providences.events.plans.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.plans.dto.CreatePlanDTO;
import com.providences.events.plans.entities.AddonPlanEntity;
import com.providences.events.plans.repositories.AddonPlanRepository;

@Service
@Transactional
public class CreateAddonPlanService {

    private AddonPlanRepository addonPlanRepository;

    public CreateAddonPlanService(AddonPlanRepository addonPlanRepository) {
        this.addonPlanRepository = addonPlanRepository;
    }

    public CreatePlanDTO.Response execute(CreatePlanDTO.Request data) {

        AddonPlanEntity plan = new AddonPlanEntity();
        plan.setName(data.getName());
        plan.setDescription(data.getDescription());
        plan.setResources(data.getResources());
        plan.setPriceMonthly(data.getPriceMonthly());
        plan.setPriceYearly(data.getPriceYearly());
        plan.setFeatures(data.getFeatures());
        plan.setLevel(data.getLevel());

        AddonPlanEntity saved = addonPlanRepository.save(plan);

        return new CreatePlanDTO.Response(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getResources(),
                saved.getPriceMonthly(),
                saved.getPriceYearly(),
                saved.getFeatures(),
                saved.getLevel());
    }
}
