package com.providences.events.album.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.providences.events.album.services.DeleteMediaOfAlbumService;
import com.providences.events.config.JWTUserData;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/medias")
public class DeleteMediaOfAlbum {

    private final DeleteMediaOfAlbumService deleteMediaOfAlbumService;

    public DeleteMediaOfAlbum(DeleteMediaOfAlbumService deleteMediaOfAlbumService) {
        this.deleteMediaOfAlbumService = deleteMediaOfAlbumService;
    }

    @DeleteMapping("/{mediaId}/album/{albumId}")
    public String uploadMultiple(
            @PathVariable(value = "albumId",required = true) String albumId,
            @PathVariable(value="mediaId",required = true) String mediaId,
            Authentication authentication) {
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        String userId = userData.getUserId();

        return deleteMediaOfAlbumService.execute(albumId, mediaId, userId);
    }

}
