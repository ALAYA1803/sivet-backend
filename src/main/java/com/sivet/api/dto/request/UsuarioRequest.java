package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos de entrada para crear un usuario. El {@code password} se recibe en texto
 * plano solo en la entrada y se almacena hasheado; nunca se expone en respuestas.
 * El tenant ({@code clinica_id}) lo deriva el backend del contexto.
 */
public record UsuarioRequest(

        @NotBlank String username,

        @NotBlank String password,

        @NotBlank String nombre,

        @NotBlank String rol
) {
}
