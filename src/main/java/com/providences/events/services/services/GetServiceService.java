package com.providences.events.services.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class GetServiceService {

    private final ServiceRepository serviceRepository;

    public GetServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceDTO.Response execute(String serviceId) {

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        return ServiceDTO.Response.response(service);
    }
}
