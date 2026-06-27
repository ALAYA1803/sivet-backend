package com.sivet.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Especie de la mascota. Contrato del frontend: 'Canino' | 'Felino' | 'Otros'.
 */
public enum Especie {

    CANINO("Canino"),
    FELINO("Felino"),
    OTROS("Otros");

    private final String value;

    Especie(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Especie fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Especie inválida: " + value));
    }
}
