package com.sivet.api.security.filters;

import com.sivet.api.security.AuthenticatedUser;
import com.sivet.api.security.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro multi-tenant de hierro (§3.1). Se ejecuta DESPUÉS de validar el JWT.
 * <p>Para cada petición autenticada que opera sobre recursos de negocio:
 * <ol>
 *   <li>Exige el header {@code X-Tenant-ID}; si falta ⇒ 403.</li>
 *   <li>Lo compara con el claim {@code veterinaria_id} del token; si difieren ⇒ 403.
 *       (Nunca se confía solo en el header: el JWT firmado es la autoridad.)</li>
 *   <li>Si coinciden, guarda el tenant en {@link TenantContext} para los controladores.</li>
 * </ol>
 * Las rutas de login ({@code /auth/**}) y de carga de clínica ({@code /clinicas/**})
 * están exentas del header (§2): la pertenencia se valida en su controlador.
 */
public class TenantAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser user
                    && requiereTenant(request)) {

                String headerValue = request.getHeader(HEADER);
                if (headerValue == null || headerValue.isBlank()) {
                    rechazar(response, "Falta el header X-Tenant-ID");
                    return;
                }

                UUID tenant;
                try {
                    tenant = UUID.fromString(headerValue.trim());
                } catch (IllegalArgumentException ex) {
                    rechazar(response, "El header X-Tenant-ID no es un UUID válido");
                    return;
                }

                if (!tenant.equals(user.veterinariaId())) {
                    rechazar(response, "El X-Tenant-ID no coincide con el tenant del token");
                    return;
                }

                TenantContext.setTenantId(tenant);
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // evita fugas entre hilos reutilizados
        }
    }

    /** Rutas exentas de la validación del header de tenant. */
    private boolean requiereTenant(HttpServletRequest request) {
        String path = request.getRequestURI();
        // - /auth, /clinicas: login y (re)carga del propio tenant.
        // - /admin-sivet: backoffice del SUPERADMIN, opera fuera de un tenant concreto.
        // - cambiar-password-inicial: opera sobre la cuenta del token, no sobre el tenant.
        return !(path.startsWith("/auth")
                || path.startsWith("/clinicas")
                || path.startsWith("/admin-sivet")
                || path.equals("/usuarios/cambiar-password-inicial"));
    }

    private void rechazar(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"status\":403,\"error\":\"Forbidden\",\"message\":\"" + mensaje + "\"}");
    }
}
