package com.sivet.api.exception;

/**
 * Violación de una regla de negocio (invariante de dominio). Se mapeará a
 * HTTP 422/400 en la capa web. Ejemplos: stock insuficiente al vender (§3.2),
 * receta sin ítems (§3.4), franja horaria inválida (§3.3), o violación de la
 * regla de integridad de inventario de productos/servicios (§1.7).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
