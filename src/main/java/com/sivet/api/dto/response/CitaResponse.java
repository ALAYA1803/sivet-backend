package com.sivet.api.dto.response;

import com.sivet.api.domain.enums.EstadoCita;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Representación de salida de una cita. FKs planas.
 */
public record CitaResponse(
        UUID id,
        UUID mascotaId,
        UUID clienteId,
        LocalDate fecha,
        String hora,
        String motivo,
        EstadoCita estado
) {
}
