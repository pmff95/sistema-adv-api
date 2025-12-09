package com.example.sistema_adv_api.user;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdatePermissionsRequest(
        @NotNull List<String> permissions
) {
}
