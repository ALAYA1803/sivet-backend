package com.sivet.api.dto.response;

/**
 * Línea de receta de salida (objeto embebido).
 */
public record RecetaItemResponse(
        String medicamento,
        String dosis,
        String via,
        String duracion,
        String indicaciones
) {
}
