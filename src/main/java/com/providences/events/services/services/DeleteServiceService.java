package com.providences.events.services.services;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional(readOnly = true)
public class DeleteServiceService {

    private final ServiceRepository serviceRepository;
    private final FetchServicesService fetchServicesService;

    @PersistenceContext
    private EntityManager em;

    public DeleteServiceService(ServiceRepository serviceRepository, FetchServicesService fetchServicesService) {
        this.serviceRepository = serviceRepository;
        this.fetchServicesService = fetchServicesService;
    }

    public Set<ServiceDTO.Response> execute(String serviceId, String userId) {

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        SupplierEntity supplier = service.getSupplier();

        if (!supplier.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado!", HttpStatus.FORBIDDEN);
        }

        serviceRepository.delete(service);

        em.flush();
        em.clear();

        return fetchServicesService.execute(supplier.getId());
    }
}
