package com.providences.events.organizer.services;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.providences.events.album.entities.MediaEntity;
import com.providences.events.album.entities.MediaEntity.MediaType;
import com.providences.events.album.repositories.MediaRepository;
import com.providences.events.config.UploadService;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class AddPictureService {
    private final OrganizerRepository organizerRepository;
    private final UploadService uploadService;
    private final MediaRepository mediaRepository;

    public AddPictureService(OrganizerRepository organizerRepository, UploadService uploadService,
            MediaRepository mediaRepository) {
        this.organizerRepository = organizerRepository;
        this.uploadService = uploadService;
        this.mediaRepository = mediaRepository;
    }

    public OrganizerDTO.Response execute(String organizerId, String userId, MultipartFile file) {
        OrganizerEntity organizer = organizerRepository.findId(organizerId)
                .orElseThrow(() -> new BusinessException("Organizador não encontrado!", HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado!", HttpStatus.FORBIDDEN);
        }

        String oldProfilePictureUrl = organizer.getProfilePicture();
        if (oldProfilePictureUrl != null && !oldProfilePictureUrl.isBlank()) {
            try {
                uploadService.deleteFileByUrl(oldProfilePictureUrl);
                
            } catch (Exception e) {
                System.err.println("Erro ao deletar arquivo antigo do Firebase: " + e.getMessage());
            }
        }

        String url;
        try {
            url = uploadService.uploadSingle(file);
        } catch (IOException e) {
            throw new BusinessException("Erro ao fazer upload: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaEntity media = new MediaEntity();
        media.setFileUrl(url);
        media.setServiceAlbum(null);
        media.setSupplier(null);
        media.setOrganizer(organizer);
        media.setMediaType(MediaType.IMAGE);
        mediaRepository.save(media);

        organizer.setProfilePicture(url);

        return OrganizerDTO.Response.response2(organizerRepository.save(organizer));

    }
}
