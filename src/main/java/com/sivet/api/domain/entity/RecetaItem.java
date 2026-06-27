package com.sivet.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Línea de una receta médica. Objeto embebido (sin id ni endpoint propio):
 * se persiste como fila de la tabla hija {@code receta_items}.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecetaItem {

    @Column(nullable = false)
    private String medicamento;

    @Column(nullable = false)
    private String dosis;

    @Column(nullable = false)
    private String via;

    @Column(nullable = false)
    private String duracion;

    @Column(nullable = false)
    private String indicaciones;
}
