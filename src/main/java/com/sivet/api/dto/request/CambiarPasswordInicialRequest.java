package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo del POST /usuarios/cambiar-password-inicial: la nueva clave que el usuario
 * elige para reemplazar la temporal entregada en el onboarding.
 */
public record CambiarPasswordInicialRequest(

        @NotBlank
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String nuevaPassword
) {
}
