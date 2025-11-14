package com.providences.events.album.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.providences.events.album.dto.AddMediaAlbumDto;
import com.providences.events.album.entities.AlbumEntity;
import com.providences.events.album.entities.MediaEntity;
import com.providences.events.album.entities.MediaEntity.MediaType;
import com.providences.events.album.repositories.AlbumRepository;
import com.providences.events.album.repositories.MediaRepository;
import com.providences.events.config.UploadService;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class AddMediaService {

    private final MediaRepository mediaRepository;
    private final UploadService uploadService;
    private final AlbumRepository albumRepository;

    public AddMediaService(
            MediaRepository mediaRepository,
            UploadService uploadService,
            AlbumRepository albumRepository) {
        this.mediaRepository = mediaRepository;
        this.uploadService = uploadService;
        this.albumRepository = albumRepository;
    }

    public List<AddMediaAlbumDto.Response> execute(String albumId, MultipartFile[] files, String userId) {

        if (files == null || files.length == 0) {
            throw new BusinessException("Nenhum ficheiro enviado", HttpStatus.BAD_REQUEST);
        }

        AlbumEntity album = albumRepository.findId(albumId)
                .orElseThrow(() -> new BusinessException("Álbum não encontrado", HttpStatus.BAD_REQUEST));

        if (!album.getService().getSupplier().getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado", HttpStatus.FORBIDDEN);
        }

        List<String> urls = new ArrayList<>();

        try {
            urls = uploadService.uploadMultiple(files);
        } catch (IOException e) {
            throw new BusinessException("Erro ao fazer upload: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<MediaEntity> medias = new ArrayList<>();

        for (String url : urls) {
            MediaEntity media = new MediaEntity();
            media.setFileUrl(url);
            media.setServiceAlbum(album);
            media.setMediaType(MediaType.IMAGE);

            medias.add(mediaRepository.save(media));
        }

        return medias.stream().map(AddMediaAlbumDto.Response::response).toList();
    }
}
