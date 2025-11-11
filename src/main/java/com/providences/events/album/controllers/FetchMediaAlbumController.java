package com.providences.events.album.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.album.dto.AddMediaAlbumDto;
import com.providences.events.album.services.FetchMediaAlbumService;

@RestController
@RequestMapping("/medias")
public class FetchMediaAlbumController {
    private FetchMediaAlbumService fetchMediaAlbumService;

    public FetchMediaAlbumController(FetchMediaAlbumService fetchMediaAlbumService) {
        this.fetchMediaAlbumService = fetchMediaAlbumService;
    }

    @GetMapping("/album/{albumId}")
    public Set<AddMediaAlbumDto.Response> get(@PathVariable(required = true) String albumId) {

        return fetchMediaAlbumService.execute(albumId);
    }
}
