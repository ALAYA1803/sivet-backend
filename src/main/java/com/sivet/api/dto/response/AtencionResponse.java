package com.sivet.api.dto.response;

import com.sivet.api.domain.enums.TipoAtencion;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representación de salida de una atención. FKs planas; {@code recetaId} presente
 * solo si la atención emitió receta.
 */
public record AtencionResponse(
        UUID id,
        UUID mascotaId,
        LocalDateTime fecha,
        TipoAtencion tipo,
        String motivo,
        String diagnostico,
        String tratamiento,
        String veterinario,
        Double temperatura,
        Integer frecCardiaca,
        Integer frecRespiratoria,
        UUID recetaId
) {
}
