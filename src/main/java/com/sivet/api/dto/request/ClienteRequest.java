package com.sivet.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Datos de entrada para crear un cliente (dueño). El tenant lo estampa el backend.
 */
public record ClienteRequest(

        @NotBlank String nombre,

        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
        String dni,

        @NotBlank String telefono,

        @NotBlank @Email String email,

        @NotBlank String direccion
) {
}
