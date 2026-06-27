package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Datos de entrada para crear un estudio (reporte de texto). Sin archivos adjuntos.
 * El tenant lo estampa el backend.
 */
public record EstudioRequest(

        @NotNull UUID mascotaId,

        @NotBlank String titulo,

        @NotBlank String tag,

        @NotBlank String fecha,

        @NotBlank String veterinario
) {
}
