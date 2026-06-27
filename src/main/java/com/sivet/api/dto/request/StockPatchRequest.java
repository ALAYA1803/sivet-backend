package com.sivet.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Cuerpo del PATCH /productos/{id} para ajustar inventario
 * (descontar/restaurar stock).
 */
public record StockPatchRequest(

        @NotNull @PositiveOrZero Integer stock
) {
}
