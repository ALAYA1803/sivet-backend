package com.sivet.api.security;

import com.sivet.api.exception.InvalidCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utilidades para que los controladores obtengan el usuario autenticado y el tenant
 * de la petición en curso, sin acoplarse a los detalles de Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static AuthenticatedUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser user) {
            return user;
        }
        throw new InvalidCredentialsException("No hay un usuario autenticado");
    }

    /**
     * Tenant validado por el {@code TenantAuthenticationFilter}. Si por la ruta no se
     * exigió el header {@code X-Tenant-ID}, recae en el {@code veterinaria_id} del token.
     */
    public static UUID currentTenantId() {
        UUID fromHeader = TenantContext.getTenantId();
        return fromHeader != null ? fromHeader : currentUser().veterinariaId();
    }
}
