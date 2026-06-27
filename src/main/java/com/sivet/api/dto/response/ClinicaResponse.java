package com.sivet.api.dto.response;

import java.util.UUID;

/**
 * Representación de salida de una clínica.
 */
public record ClinicaResponse(
        UUID id,
        String nombre,
        String sede,
        String ruc,
        String telefono,
        String email,
        String direccion
) {
}
