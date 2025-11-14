package com.providences.events.services.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.services.ServiceEntity;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional(readOnly = true)
public class FetchServicesService {

    private SupplierRepository supplierRepository;
    public FetchServicesService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Set<ServiceDTO.Response> execute(String supplierId) {
        SupplierEntity supplier = supplierRepository.findId(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        Set<ServiceEntity> services = supplier.getServices();

        return services.stream().map(ServiceDTO.Response::response).collect(Collectors.toSet());
    }
}
