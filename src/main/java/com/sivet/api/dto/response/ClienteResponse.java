package com.sivet.api.dto.response;

import java.util.UUID;

/**
 * Representación de salida de un cliente. No expone el tenant (clinica_id es interno).
 */
public record ClienteResponse(
        UUID id,
        String nombre,
        String dni,
        String telefono,
        String email,
        String direccion
) {
}
