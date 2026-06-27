package com.sivet.api.dto.response;

/**
 * Respuesta del POST /auth/login: token firmado + payload con los claims.
 * <p>{@code requiereCambioPassword} se expone explícitamente para que el frontend
 * redirija al cambio de clave inicial tras un onboarding B2B.
 */
public record LoginResponse(
        String token,
        JwtPayload payload,
        boolean requiereCambioPassword
) {
}
