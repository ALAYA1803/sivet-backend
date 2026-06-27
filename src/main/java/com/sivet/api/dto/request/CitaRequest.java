package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Datos de entrada para agendar una cita. El {@code estado} NO se recibe:
 * el backend lo fija siempre en 'pendiente' al crear. El tenant lo estampa el backend.
 */
public record CitaRequest(

        @NotNull UUID mascotaId,

        @NotNull UUID clienteId,

        @NotNull LocalDate fecha,

        @NotBlank
        @Pattern(regexp = "([01]\\d|2[0-3]):[0-5]\\d", message = "La hora debe tener formato HH:mm")
        String hora,

        @NotBlank String motivo
) {
}
