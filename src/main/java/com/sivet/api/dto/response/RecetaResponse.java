package com.sivet.api.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * Representación de salida de una receta con sus líneas embebidas.
 */
public record RecetaResponse(
        UUID id,
        UUID atencionId,
        List<RecetaItemResponse> items
) {
}
