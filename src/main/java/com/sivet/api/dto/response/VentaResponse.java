package com.sivet.api.dto.response;

import com.sivet.api.domain.enums.EstadoVenta;
import com.sivet.api.domain.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Representación de salida de una venta con sus líneas embebidas.
 * {@code motivoAnulacion} presente solo cuando estado == 'anulada'.
 */
public record VentaResponse(
        UUID id,
        LocalDateTime fecha,
        UUID clienteId,
        List<VentaItemResponse> items,
        BigDecimal total,
        MetodoPago metodoPago,
        EstadoVenta estado,
        String vendedor,
        String motivoAnulacion
) {
}
