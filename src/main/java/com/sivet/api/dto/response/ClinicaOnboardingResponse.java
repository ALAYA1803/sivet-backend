package com.sivet.api.dto.response;

import java.util.UUID;

/**
 * Resultado del onboarding B2B. Contiene la {@code passwordTemporal} EN CLARO: es la
 * única vez que viaja sin hashear, para que el frontend se la muestre al SUPERADMIN.
 * No se vuelve a poder recuperar (solo se persiste su hash).
 */
public record ClinicaOnboardingResponse(
        UUID clinicaId,
        String username,
        String passwordTemporal
) {
}
