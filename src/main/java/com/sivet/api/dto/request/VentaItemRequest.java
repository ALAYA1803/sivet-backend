package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Línea de venta de entrada (objeto embebido, snapshot de nombre/precio).
 */
public record VentaItemRequest(

        @NotNull UUID productoId,

        // nombre/precio son snapshot; el backend puede revalidarlos contra el producto.
        String nombre,

        @NotNull @Positive Integer cantidad,

        @NotNull @PositiveOrZero BigDecimal precio
) {
}
