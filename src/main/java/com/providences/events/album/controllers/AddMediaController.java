package com.providences.events.album.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.providences.events.album.dto.AddMediaAlbumDto;
import com.providences.events.album.services.AddMediaService;
import com.providences.events.config.JWTUserData;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/medias")
public class AddMediaController {

    private final AddMediaService addMediaAlbumService;

    public AddMediaController(AddMediaService addMediaAlbumService) {
        this.addMediaAlbumService = addMediaAlbumService;
    }

    @PostMapping("/album/{albumId}")
    public ResponseEntity<List<AddMediaAlbumDto.Response>> uploadMultiple(
            @PathVariable("albumId") String albumId,
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication) {

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity.ok().body(addMediaAlbumService.execute(albumId, files, userId));
    }

}
