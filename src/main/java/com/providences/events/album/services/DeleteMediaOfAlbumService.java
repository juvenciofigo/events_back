package com.providences.events.album.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.providences.events.album.entities.AlbumEntity;
import com.providences.events.album.entities.MediaEntity;
import com.providences.events.album.repositories.AlbumRepository;
import com.providences.events.album.repositories.MediaRepository;
import com.providences.events.config.UploadService;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class DeleteMediaOfAlbumService {

    private final UploadService uploadService;
    private final AlbumRepository albumRepository;
    private final MediaRepository mediaRepository;

    public DeleteMediaOfAlbumService(UploadService uploadService, AlbumRepository albumRepository,
            MediaRepository mediaRepository) {
        this.uploadService = uploadService;
        this.albumRepository = albumRepository;
        this.mediaRepository = mediaRepository;
    }

    public String execute(String albumId, String mediaId, String userId) {

        AlbumEntity album = albumRepository.findId(albumId)
                .orElseThrow(() -> new BusinessException("Álbum não encontrado", HttpStatus.BAD_REQUEST));

        if (!album.getService().getSupplier().getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado", HttpStatus.FORBIDDEN);
        }

        MediaEntity media = album.getMedias().stream()
                .filter(m -> m.getId().equals(mediaId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Media não encontrada", HttpStatus.BAD_REQUEST));

        album.getMedias().remove(media);
        
        uploadService.deleteFileByUrl(media.getFileUrl());

        mediaRepository.delete(media);

        return "deleted";
    }
}
