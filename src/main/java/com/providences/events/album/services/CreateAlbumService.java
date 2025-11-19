package com.providences.events.album.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.album.dto.CreateAlbumDTO;
import com.providences.events.album.entities.AlbumEntity;
import com.providences.events.album.repositories.AlbumRepository;
import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CreateAlbumService {
        public AlbumRepository albumRepository;
        public ServiceRepository serviceRepository;

        public CreateAlbumService(AlbumRepository albumRepository, ServiceRepository serviceRepository) {
                this.albumRepository = albumRepository;
                this.serviceRepository = serviceRepository;
        }

        public CreateAlbumDTO.Response execute(CreateAlbumDTO.Request data, String userId) {
                ServiceEntity service = serviceRepository.getId(data.getServiceId())
                                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado!"));

                if (!userId.equals(service.getSupplier().getUser().getId())) {
                        throw new ForbiddenException("Sem permissão!");
                }

                AlbumEntity album = new AlbumEntity();
                album.setTitle(data.getTitle());
                album.setDescription(data.getDescription());
                album.setService(service);

                AlbumEntity savedAlbum = albumRepository.save(album);

                CreateAlbumDTO.Response responseDTO = CreateAlbumDTO.Response.response(savedAlbum, service.getId());

                return responseDTO;
        }

}
