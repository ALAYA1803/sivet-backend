package com.sivet.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Estado de una venta. Contrato: 'completada' | 'anulada'.
 * Al crear siempre es 'completada' (lo fija el flujo de negocio).
 */
public enum EstadoVenta {

    COMPLETADA("completada"),
    ANULADA("anulada");

    private final String value;

    EstadoVenta(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EstadoVenta fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado de venta inválido: " + value));
    }
}
