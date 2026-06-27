package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Línea de receta de entrada (objeto embebido, sin id propio).
 * Reutilizado por {@link RecetaRequest} y por la receta embebida en
 * {@link AtencionRequest}.
 */
public record RecetaItemRequest(

        @NotBlank String medicamento,

        @NotBlank String dosis,

        @NotBlank String via,

        @NotBlank String duracion,

        @NotBlank String indicaciones
) {
}
