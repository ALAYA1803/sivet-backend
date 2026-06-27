package com.sivet.api.dto.response;

/**
 * Respuesta del POST /auth/login: token firmado + payload con los claims.
 */
public record LoginResponse(
        String token,
        JwtPayload payload
) {
}
