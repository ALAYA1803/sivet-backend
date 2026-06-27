package com.sivet.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 * Cuerpo del PATCH /clinicas/{id}. Actualización parcial: todos los campos son
 * opcionales y solo se aplican los que vengan informados (no {@code null}).
 * La {@code sede} no es editable por esta vía.
 */
public record ClinicaPatchRequest(

        String nombre,

        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos")
        String ruc,

        String telefono,

        @Email String email,

        String direccion
) {
}
