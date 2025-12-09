package com.example.sistema_adv_api.user;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Role role,
        Set<String> permissions
) {
}
