package com.example.sistema_adv_api.client;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        String email,
        String phone,
        OffsetDateTime createdAt
) {
}
