package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo del POST /auth/login (endpoint seguro recomendado por el contrato §3.1).
 * {@code credencial} = username.
 */
public record LoginRequest(

        @NotBlank String credencial,

        @NotBlank String password
) {
}
