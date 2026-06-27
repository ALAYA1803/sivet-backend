package com.sivet.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Categoría del producto/servicio. Contrato:
 * 'Medicamento' | 'Antiparasitario' | 'Antiinflamatorio' | 'Vacuna' |
 * 'Alimento' | 'Accesorio' | 'Servicio'.
 * Regla de integridad: 'Servicio' ⇒ stock y stockMin son null (sin inventario).
 */
public enum CategoriaProducto {

    MEDICAMENTO("Medicamento"),
    ANTIPARASITARIO("Antiparasitario"),
    ANTIINFLAMATORIO("Antiinflamatorio"),
    VACUNA("Vacuna"),
    ALIMENTO("Alimento"),
    ACCESORIO("Accesorio"),
    SERVICIO("Servicio");

    private final String value;

    CategoriaProducto(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CategoriaProducto fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Categoría de producto inválida: " + value));
    }
}
