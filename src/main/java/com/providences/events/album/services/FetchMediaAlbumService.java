package com.providences.events.album.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.album.dto.AddMediaAlbumDto;
import com.providences.events.album.entities.AlbumEntity;
import com.providences.events.album.repositories.AlbumRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchMediaAlbumService {
    private final AlbumRepository albumRepository;

    public FetchMediaAlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Set<AddMediaAlbumDto.Response> execute(String albumId) {
        AlbumEntity album = albumRepository.findMediasByAlbumId(albumId)
                .orElseThrow(() -> new BusinessException("Album não encontrado!", HttpStatus.NOT_FOUND));

        return album.getMedias().stream().map(AddMediaAlbumDto.Response::response).collect(Collectors.toSet());
    }
}
