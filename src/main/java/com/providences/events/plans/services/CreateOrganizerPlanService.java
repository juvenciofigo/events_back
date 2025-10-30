package com.providences.events.plans.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.plans.dto.CreatePlanDTO;
import com.providences.events.plans.entities.OrganizerPlanEntity;
import com.providences.events.plans.repositories.OrganizerPlanRepository;

@Service
@Transactional
public class CreateOrganizerPlanService {

    private  OrganizerPlanRepository organizerPlanRepository;
    

    public CreateOrganizerPlanService(OrganizerPlanRepository organizerPlanRepository) {
        this.organizerPlanRepository = organizerPlanRepository;
    }

    public CreatePlanDTO.Response execute(CreatePlanDTO.Request data) {

        OrganizerPlanEntity plan = new OrganizerPlanEntity();
        plan.setName(data.getName());
        plan.setDescription(data.getDescription());
        plan.setResources(data.getResources());
        plan.setPriceMonthly(data.getPriceMonthly());
        plan.setPriceYearly(data.getPriceYearly());
        plan.setFeatures(data.getFeatures());
        plan.setLevel(data.getLevel());

        OrganizerPlanEntity saved = organizerPlanRepository.save(plan);

        return CreatePlanDTO.Response.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .resources(saved.getResources())
                .priceMonthly(saved.getPriceMonthly())
                .priceYearly(saved.getPriceYearly())
                .features(saved.getFeatures())
                .level(saved.getLevel())
                .build();
    }
}
