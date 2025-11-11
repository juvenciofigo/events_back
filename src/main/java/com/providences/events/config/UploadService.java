package com.providences.events.config;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService {

    public List<String> uploadMultiple(MultipartFile[] files)
            throws IOException {
        List<String> urls = new ArrayList<>();

        Bucket bucket = StorageClient.getInstance().bucket();

        for (MultipartFile file : files) {
            if (file.isEmpty())
                continue; 
                
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Faz upload para o bucket
            bucket.create(fileName, file.getBytes(), file.getContentType());

            // Codifica o nome para URL segura
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            // Monta URL pública
            String publicUrl = String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    bucket.getName(),
                    encodedFileName);

            urls.add(publicUrl);
        }

        // Retorna lista de URLs
        return urls;
    }
}