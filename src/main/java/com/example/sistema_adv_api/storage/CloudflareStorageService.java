package com.example.sistema_adv_api.storage;

import com.example.sistema_adv_api.config.CloudflareStorageProperties;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class CloudflareStorageService {

    private final S3Client s3Client;
    private final CloudflareStorageProperties properties;

    public CloudflareStorageService(S3Client s3Client, CloudflareStorageProperties properties) {
        this.s3Client = s3Client;
        this.properties = properties;
    }

    public UploadedObject uploadClientFile(UUID clientId, MultipartFile file) {
        String key = buildObjectKey(clientId, file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(properties.getBucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, file.getSize()));

            String publicUrl = properties.getPublicBaseUrl() != null && !properties.getPublicBaseUrl().isBlank()
                    ? properties.getPublicBaseUrl().replaceAll("/$", "") + "/" + key
                    : null;

            return new UploadedObject(key, publicUrl);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao fazer upload do arquivo para a Cloudflare", e);
        }
    }

    private String buildObjectKey(UUID clientId, String originalName) {
        String safeName = originalName == null || originalName.isBlank() ? "arquivo" : originalName;
        return "clients/" + clientId + "/" + Instant.now().toEpochMilli() + "_" + safeName;
    }

    public record UploadedObject(String key, String publicUrl) {
    }
}
