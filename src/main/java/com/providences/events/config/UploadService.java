package com.providences.events.config;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {
    public List<String> uploadMultiple(MultipartFile[] files) throws IOException {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty())
                continue;

            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(fileName, file.getBytes(), file.getContentType());

            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            String publicUrl = String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    bucket.getName(),
                    encodedFileName);

            urls.add(publicUrl);
        }

        return urls;
    }

    public String uploadSingle(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("O ficheiro enviado está vazio ou é inválido.");
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create(fileName, file.getBytes(), file.getContentType());

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                encodedFileName);
    }

    // Deletar arquivo por URL
    public void deleteFileByUrl(String fileUrl) {
        try {
            // Extrair o nome do arquivo da URL
            String[] parts = fileUrl.split("/o/");
            if (parts.length < 2) {
                throw new IllegalArgumentException("URL inválida do Firebase Storage");
            }

            String filePath = parts[1].split("\\?")[0];
            String decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.name());

            // Buscar arquivo no bucket
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(decodedPath);

            if (blob != null && blob.exists()) {
                blob.delete();
                System.out.println("✅ Arquivo deletado com sucesso: " + decodedPath);
            } else {
                System.out.println("⚠️ Arquivo não encontrado no Firebase: " + decodedPath);
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao decodificar URL do Firebase", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do Firebase", e);
        }
    }

    public void deleteMultipleFiles(Set<String> urls) {
        for (String url : urls) {
            deleteFileByUrl(url);
        }
    }

}
