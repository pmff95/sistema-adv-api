package com.example.sistema_adv_api.client;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientFileResponse(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        String url,
        OffsetDateTime uploadedAt
) {
}
