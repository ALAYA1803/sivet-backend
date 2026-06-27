package com.sivet.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Línea de venta de salida (snapshot de nombre/precio al momento de vender).
 */
public record VentaItemResponse(
        UUID productoId,
        String nombre,
        Integer cantidad,
        BigDecimal precio
) {
}
