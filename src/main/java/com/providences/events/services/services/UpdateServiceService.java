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

@Service
@Transactional(readOnly = true)
public class UpdateServiceService {

    private final ServiceRepository serviceRepository;

    public UpdateServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceDTO.Response execute(String serviceId, ServiceDTO.Update data, String userId) {

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        SupplierEntity supplier = service.getSupplier();

        if (!supplier.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado!", HttpStatus.FORBIDDEN);
        }

        service.setCategory(data.getCategory());
        service.setDescription(data.getDescription());
        service.setPriceBase(data.getPriceBase());

        return ServiceDTO.Response.response(serviceRepository.save(service));
    }
}
