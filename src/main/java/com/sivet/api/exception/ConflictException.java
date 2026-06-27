package com.sivet.api.exception;

/**
 * Conflicto con el estado actual del recurso. Se mapeará a HTTP 409 en la capa web.
 * <p>Caso principal: colisión de agenda (§3.3) — ya existe una cita activa para la
 * misma tupla (clínica, fecha, hora).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
