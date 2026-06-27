package com.sivet.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Estado de una cita de la agenda. Contrato:
 * 'pendiente' | 'completada' | 'cancelada'. Al crear siempre es 'pendiente'.
 */
public enum EstadoCita {

    PENDIENTE("pendiente"),
    COMPLETADA("completada"),
    CANCELADA("cancelada");

    private final String value;

    EstadoCita(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EstadoCita fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado de cita inválido: " + value));
    }
}
