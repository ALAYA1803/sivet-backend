package com.sivet.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Datos de entrada para crear una receta vinculada a una atención.
 * Soporta el flujo actual del frontend (POST /recetas con el atencionId).
 * El tenant lo estampa el backend.
 */
public record RecetaRequest(

        @NotNull UUID atencionId,

        @NotEmpty(message = "La receta debe tener al menos un ítem")
        @Valid
        List<RecetaItemRequest> items
) {
}
