package com.sivet.api.dto.response;

/**
 * Resultado del alta de personal. Incluye la {@code passwordTemporal} EN CLARO (única
 * vez) para que el ADMIN_CLINICA se la entregue al empleado; solo se persiste su hash.
 */
public record EmpleadoCreadoResponse(
        String username,
        String passwordTemporal
) {
}
