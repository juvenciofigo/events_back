package com.providences.events.services.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional
public class RegisterServiceService {

    private final SupplierRepository supplierRepository;
    private final ServiceRepository serviceRepository;

    public RegisterServiceService(SupplierRepository supplierRepository, ServiceRepository serviceRepository) {
        this.supplierRepository = supplierRepository;
        this.serviceRepository = serviceRepository;
    }

    public ServiceDTO.Response execute(ServiceDTO.Request data, String userId) {

        SupplierEntity supplier = supplierRepository.findById(data.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));

        if (!supplier.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autorizado!", HttpStatus.FORBIDDEN);
        }

        ServiceEntity service = new ServiceEntity();
        service.setCategory(data.getCategory());

        service.setDescription(
                data.getDescription() != null && !data.getDescription().isBlank()
                        ? data.getDescription()
                        : "");
        service.setPriceBase(data.getPriceBase());
        service.setSupplier(supplier);

        return ServiceDTO.Response.response(serviceRepository.save(service));
    }
}
