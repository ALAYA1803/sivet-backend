package com.sivet.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Tipo de atención médica. Contrato:
 * 'Consulta general' | 'Vacunación' | 'Desparasitación' | 'Cirugía'.
 */
public enum TipoAtencion {

    CONSULTA_GENERAL("Consulta general"),
    VACUNACION("Vacunación"),
    DESPARASITACION("Desparasitación"),
    CIRUGIA("Cirugía");

    private final String value;

    TipoAtencion(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoAtencion fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de atención inválido: " + value));
    }
}
