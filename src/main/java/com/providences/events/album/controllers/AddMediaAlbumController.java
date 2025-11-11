package com.providences.events.album.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.providences.events.album.dto.AddMediaAlbumDto;
import com.providences.events.album.services.AddMediaAlbumService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/medias")
public class AddMediaAlbumController {

    private final AddMediaAlbumService addMediaAlbumService;

    public AddMediaAlbumController(AddMediaAlbumService addMediaAlbumService) {
        this.addMediaAlbumService = addMediaAlbumService;
    }

    @PostMapping("/album/{albumId}")
    public List<AddMediaAlbumDto.Response> uploadMultiple(@PathVariable String albumId,
            @RequestParam("files") MultipartFile[] files) {

        return addMediaAlbumService.execute(albumId, files);
    }

}
