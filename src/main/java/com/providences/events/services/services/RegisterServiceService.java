package com.providences.events.services.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.services.dto.RegisterServiceDTO;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional
public class RegisterServiceService {

    private SupplierRepository supplierRepository;

    private ServiceRepository serviceRepository;

    public RegisterServiceService(SupplierRepository supplierRepository, ServiceRepository serviceRepository) {
        this.supplierRepository = supplierRepository;
        this.serviceRepository = serviceRepository;
    }

    public RegisterServiceDTO.Response execute(RegisterServiceDTO.Request data) {
        SupplierEntity supplier = supplierRepository.findById(data.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        ServiceEntity service = new ServiceEntity();
        service.setCategory(data.getCategory());
        service.setDescription(data.getDescription());
        service.setPriceBase(data.getPriceBase());
        service.setSupplier(supplier);

        ServiceEntity savedService = serviceRepository.save(service);
        RegisterServiceDTO.Response responseDTO = new RegisterServiceDTO.Response(
                savedService.getId(),
                savedService.getCategory(),
                savedService.getDescription(),
                savedService.getPriceBase());

        return responseDTO;
    }
}
