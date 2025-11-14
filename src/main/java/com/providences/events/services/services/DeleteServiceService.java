package com.providences.events.services.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.album.entities.AlbumEntity;
import com.providences.events.album.services.DeleteAlbumService;
import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.services.dto.ServiceDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class DeleteServiceService {

    private final ServiceRepository serviceRepository;
    private final FetchServicesService fetchServicesService;
    private final DeleteAlbumService deleteAlbumService;

    @PersistenceContext
    private EntityManager em;

    public DeleteServiceService(
            ServiceRepository serviceRepository,
            FetchServicesService fetchServicesService,
            DeleteAlbumService deleteAlbumService) {
        this.serviceRepository = serviceRepository;
        this.fetchServicesService = fetchServicesService;
        this.deleteAlbumService = deleteAlbumService;
    }

    public Set<ServiceDTO.Response> execute(String serviceId, String userId) {

        ServiceEntity service = serviceRepository.getService(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        SupplierEntity supplier = service.getSupplier();

        if (!supplier.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado!", HttpStatus.FORBIDDEN);
        }

        Set<AlbumEntity> albums = new HashSet<>(service.getAlbums());

        if (albums.size() > 0) {
            for (AlbumEntity album : albums) {

                deleteAlbumService.execute(album.getId(), userId);
            }
        }

        serviceRepository.delete(service);

        em.flush();
        em.clear();

        return fetchServicesService.execute(supplier.getId());
    }
}
