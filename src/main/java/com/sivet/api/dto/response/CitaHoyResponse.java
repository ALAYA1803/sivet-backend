package com.sivet.api.dto.response;

/**
 * Read-model del dashboard: citas del día actual (no canceladas) con nombres
 * denormalizados de mascota/cliente/veterinario.
 */
public record CitaHoyResponse(
        String hora,
        String mascota,
        String cliente,
        String tipo,
        String vet
) {
}
