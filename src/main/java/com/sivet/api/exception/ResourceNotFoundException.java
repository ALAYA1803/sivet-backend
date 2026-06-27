package com.sivet.api.exception;

/**
 * Recurso inexistente dentro del tenant. Se mapeará a HTTP 404 en la capa web.
 * <p>Por aislamiento multi-tenant (§3.1), también se usa cuando un registro existe
 * pero pertenece a otra clínica: se responde 404 (no 403) para no filtrar existencia.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String recurso, Object id) {
        return new ResourceNotFoundException(recurso + " no encontrado: " + id);
    }
}
