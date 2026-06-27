package com.sivet.api.exception;

/**
 * Credenciales inválidas o ausencia de autenticación. Se mapeará a HTTP 401.
 * El mensaje hacia el cliente es genérico para no revelar si el usuario existe (§3.1).
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
