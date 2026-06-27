package com.sivet.api.dto.response;

import java.util.UUID;

/**
 * Representación de salida de un usuario. NUNCA incluye el password (ni su hash).
 */
public record UsuarioResponse(
        UUID id,
        String username,
        String nombre,
        String rol,
        UUID clinicaId
) {
}
