package com.sivet.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Alta B2B de una clínica + su doctor principal (ADMIN_CLINICA), ejecutada por el
 * SUPERADMIN. La contraseña no se recibe: el backend la genera y la devuelve una vez.
 */
public record ClinicaOnboardingRequest(

        // --- Datos de la clínica ---
        @NotBlank String nombre,

        @NotBlank String sede,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos")
        String ruc,

        @NotBlank String telefono,

        @NotBlank @Email String email,

        @NotBlank String direccion,

        // --- Doctor principal (cuenta ADMIN_CLINICA) ---
        @NotBlank String doctorNombre,

        @NotBlank String doctorUsername
) {
}
