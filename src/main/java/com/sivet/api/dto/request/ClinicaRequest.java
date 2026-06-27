package com.sivet.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Datos de entrada para crear/actualizar una clínica (tenant).
 */
public record ClinicaRequest(

        @NotBlank String nombre,

        @NotBlank String sede,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos")
        String ruc,

        @NotBlank String telefono,

        @NotBlank @Email String email,

        @NotBlank String direccion
) {
}
