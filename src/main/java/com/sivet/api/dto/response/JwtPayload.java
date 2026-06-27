package com.sivet.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Claims del JWT que el frontend decodifica y necesita (contrato §3.1).
 * Los nombres JSON respetan exactamente el contrato (snake_case).
 */
public record JwtPayload(

        @JsonProperty("id_usuario") UUID idUsuario,

        String nombre,

        String rol,

        @JsonProperty("veterinaria_id") UUID veterinariaId
) {
}
