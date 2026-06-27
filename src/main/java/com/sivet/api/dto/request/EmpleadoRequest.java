package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Alta de personal por parte del ADMIN_CLINICA. El tenant (clinicaId) NO se envía: se
 * hereda del token. La contraseña tampoco: la genera el backend (clave temporal).
 * <p>El {@code rol} se valida contra el conjunto permitido (p. ej. Veterinario,
 * Recepcionista) en la capa de servicio.
 */
public record EmpleadoRequest(

        @NotBlank String nombre,

        @NotBlank String username,

        @NotBlank String rol
) {
}
