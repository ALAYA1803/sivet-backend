package com.sivet.api.dto.response;

import java.util.UUID;

/**
 * Representación de salida de un estudio (reporte de texto).
 */
public record EstudioResponse(
        UUID id,
        UUID mascotaId,
        String titulo,
        String tag,
        String fecha,
        String veterinario
) {
}
