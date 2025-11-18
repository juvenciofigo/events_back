package com.providences.events.album.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.album.dto.CreateAlbumDTO;
import com.providences.events.album.services.CreateAlbumService;
import com.providences.events.config.token.JWTUserDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("albums")
public class CreateAlbumController {

    private CreateAlbumService createAlbumService;

    public CreateAlbumController(CreateAlbumService createAlbumService) {
        this.createAlbumService = createAlbumService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateAlbumDTO.Response> postMethodName(@RequestBody CreateAlbumDTO.Request data,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        CreateAlbumDTO.Response album = createAlbumService.execute(data, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(album);
    }

}
