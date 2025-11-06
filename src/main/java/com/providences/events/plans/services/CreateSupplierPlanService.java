package com.providences.events.plans.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.plans.dto.CreatePlanDTO;
import com.providences.events.plans.entities.SupplierPlanEntity;
import com.providences.events.plans.repositories.SupplierPlanRepository;

@Service
@Transactional
public class CreateSupplierPlanService {

    private final SupplierPlanRepository supplierPlanRepository;

    public CreateSupplierPlanService(SupplierPlanRepository supplierPlanRepository) {
        this.supplierPlanRepository = supplierPlanRepository;
    }

    public CreatePlanDTO.Response execute(CreatePlanDTO.Request data) {

        SupplierPlanEntity plan = new SupplierPlanEntity();
        plan.setName(data.getName());
        plan.setDescription(data.getDescription());
        plan.setResources(data.getResources());
        plan.setPriceMonthly(data.getPriceMonthly());
        plan.setPriceYearly(data.getPriceYearly());
        plan.setFeatures(data.getFeatures());
        plan.setLevel(data.getLevel());

        SupplierPlanEntity saved = supplierPlanRepository.save(plan);

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
