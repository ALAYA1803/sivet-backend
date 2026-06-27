package com.sivet.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Línea de una venta. Objeto embebido (sin id ni endpoint propio): se persiste
 * como fila de la tabla hija {@code venta_items}.
 * <p>Es un snapshot histórico: {@code nombre} y {@code precio} se congelan al
 * momento de la venta y no se re-derivan del producto actual. Por eso
 * {@code productoId} es un UUID plano (referencia histórica), no una FK gestionada.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaItem {

    /** Referencia histórica al producto (productos.id). No es FK gestionada. */
    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    /** Snapshot del nombre del producto al momento de vender. */
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer cantidad;

    /** Precio unitario aplicado (snapshot). */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
}
