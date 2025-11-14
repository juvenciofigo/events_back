package com.providences.events.album.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
public class DeleteAlbumService {
        public final AlbumRepository albumRepository;
        public final MediaRepository mediaRepository;
        private final UploadService uploadService;

        public DeleteAlbumService(AlbumRepository albumRepository, MediaRepository mediaRepository,
                        UploadService uploadService) {
                this.albumRepository = albumRepository;
                this.mediaRepository = mediaRepository;
                this.uploadService = uploadService;
        }

        public String execute(String albumId, String userId) {

                AlbumEntity album = albumRepository.findId(albumId)
                                .orElseThrow(() -> new BusinessException("Álbum não encontrado",
                                                HttpStatus.NOT_FOUND));

                if (!album.getService().getSupplier().getUser().getId().equals(userId)) {
                        throw new BusinessException("Não autoridado", HttpStatus.FORBIDDEN);
                }

                Set<MediaEntity> medias = new HashSet<>(album.getMedias());

                Set<String> urls = medias.stream()
                                .map(MediaEntity::getFileUrl)
                                .collect(Collectors.toSet());

                uploadService.deleteMultipleFiles(urls);
                albumRepository.delete(album);

                return "deleted";
        }

}
