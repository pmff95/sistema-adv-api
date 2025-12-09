package com.example.sistema_adv_api.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @Size(max = 30) String phone
) {
}
