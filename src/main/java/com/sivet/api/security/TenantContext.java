package com.sivet.api.security;

import java.util.UUID;

/**
 * Almacén por hilo del Tenant ID (clinica_id) de la petición en curso. Lo escribe
 * el {@link com.sivet.api.security.filters.TenantAuthenticationFilter} tras validar
 * que el header {@code X-Tenant-ID} coincide con el claim {@code veterinaria_id} del
 * JWT, y lo leen los controladores para pasarlo a la capa de servicio.
 * <p>El filtro limpia el contexto al finalizar la petición para evitar fugas entre
 * hilos reutilizados del pool del servidor.
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setTenantId(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static UUID getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
