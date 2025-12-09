package com.example.sistema_adv_api.auth;

import java.util.Set;
import java.util.UUID;

public record LoginResponse(
        UUID id,
        String name,
        String email,
        String role,
        Set<String> permissions
) {
}
