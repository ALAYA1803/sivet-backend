package com.sivet.api.dto.request;

import com.sivet.api.domain.enums.TipoAtencion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Datos de entrada para registrar una atención médica. El tenant lo estampa el backend.
 * <p>Soporta dos modos (§3.4 del contrato):
 * <ul>
 *   <li>Flujo encadenado del frontend: enviar {@code recetaId} de una receta ya creada.</li>
 *   <li>Endpoint atómico recomendado: enviar {@code receta} embebida (items) y dejar que
 *       el backend cree atención + receta en una sola transacción.</li>
 * </ul>
 */
public record AtencionRequest(

        @NotNull UUID mascotaId,

        @NotNull LocalDateTime fecha,

        @NotNull TipoAtencion tipo,

        @NotBlank String motivo,

        @NotBlank String diagnostico,

        @NotBlank String tratamiento,

        @NotBlank String veterinario,

        @NotNull Double temperatura,

        @NotNull Integer frecCardiaca,

        @NotNull Integer frecRespiratoria,

        /** Opcional: receta ya existente (flujo encadenado del frontend). */
        UUID recetaId,

        /** Opcional: receta embebida para creación atómica (endpoint recomendado). */
        @Valid RecetaEmbebidaRequest receta
) {

    /**
     * Receta embebida dentro de la atención (sin atencionId: lo resuelve el backend).
     */
    public record RecetaEmbebidaRequest(

            @NotEmpty(message = "La receta debe tener al menos un ítem")
            @Valid
            List<RecetaItemRequest> items
    ) {
    }
}
