package com.sivet.api.dto.response;

import com.sivet.api.domain.enums.Especie;
import com.sivet.api.domain.enums.Sexo;

import java.util.UUID;

/**
 * Representación de salida de una mascota. La FK se expone plana ({@code clienteId}).
 * {@code foto} puede ser null (sin archivos).
 */
public record MascotaResponse(
        UUID id,
        String nombre,
        Especie especie,
        String raza,
        Sexo sexo,
        String edad,
        Double peso,
        String color,
        UUID clienteId,
        String foto,
        boolean esterilizada,
        String microchip
) {
}
