package com.sivet.api.dto.request;

import com.sivet.api.domain.enums.EstadoCita;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo del PATCH /citas/{id} para cambiar el estado de una cita.
 */
public record EstadoCitaPatchRequest(

        @NotNull EstadoCita estado
) {
}
