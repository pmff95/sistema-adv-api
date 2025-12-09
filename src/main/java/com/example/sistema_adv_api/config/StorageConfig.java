package com.example.sistema_adv_api.config;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class StorageConfig {

    @Bean
    public S3Client s3Client(CloudflareStorageProperties properties) {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(properties.getAccessKeyId(), properties.getSecretKey())))
                .endpointOverride(URI.create("https://" + properties.getAccountId() + ".r2.cloudflarestorage.com"))
                .region(Region.of("auto"))
                .forcePathStyle(true)
                .build();
    }
}
