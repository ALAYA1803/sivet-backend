package com.sivet.api.exception;

/**
 * Acceso denegado a un recurso por el que el usuario SÍ está autenticado pero que
 * no le pertenece. Se mapea a HTTP 403 en la capa web.
 * <p>A diferencia de {@link ResourceNotFoundException} (404, usada para no filtrar
 * existencia en consultas), se reserva para operaciones donde el cliente conoce el
 * recurso (p. ej. su propia clínica vía el {@code veterinaria_id} del token) y se le
 * niega operar sobre uno ajeno.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
