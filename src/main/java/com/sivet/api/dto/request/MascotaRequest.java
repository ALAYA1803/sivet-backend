package com.sivet.api.dto.request;

import com.sivet.api.domain.enums.Especie;
import com.sivet.api.domain.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * Datos de entrada para crear una mascota. El tenant lo estampa el backend.
 * <p>{@code foto} es opcional (String o null) — no se manejan archivos.
 * {@code microchip} es opcional.
 */
public record MascotaRequest(

        @NotBlank String nombre,

        @NotNull Especie especie,

        @NotBlank String raza,

        @NotNull Sexo sexo,

        @NotBlank String edad,

        @NotNull @Positive Double peso,

        @NotBlank String color,

        @NotNull UUID clienteId,

        String foto,

        @NotNull Boolean esterilizada,

        String microchip
) {
}
