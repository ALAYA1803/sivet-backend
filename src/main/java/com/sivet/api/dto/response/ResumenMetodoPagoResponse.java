package com.sivet.api.dto.response;

import com.sivet.api.domain.enums.MetodoPago;

import java.math.BigDecimal;

/**
 * Read-model del dashboard: recaudación de ventas completadas agrupada por método
 * de pago. {@code color} es HEX de presentación; {@code porcentaje} en rango 0–100.
 */
public record ResumenMetodoPagoResponse(
        MetodoPago metodo,
        BigDecimal monto,
        String color,
        double porcentaje
) {
}
