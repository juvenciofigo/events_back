package com.providences.events.services.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.services.dto.ServicesBySupplierDTO;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional(readOnly = true)
public class GetServicesBySupplierService {

    private SupplierRepository supplierRepository;

    private ServiceRepository serviceRepository;

    public GetServicesBySupplierService(SupplierRepository supplierRepository, ServiceRepository serviceRepository) {
        this.supplierRepository = supplierRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<ServicesBySupplierDTO> execute(String supplierId) {
        SupplierEntity supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        List<ServiceEntity> services = serviceRepository.findBySupplier_Id(supplier.getId());

        return services.stream().map(service -> ServicesBySupplierDTO.response(service)).toList();
    }
}
