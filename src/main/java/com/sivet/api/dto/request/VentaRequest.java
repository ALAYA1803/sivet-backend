package com.sivet.api.dto.request;

import com.sivet.api.domain.enums.MetodoPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Datos de entrada para registrar una venta. El {@code estado} NO se recibe:
 * el backend lo fija siempre en 'completada' al crear. El tenant lo estampa el backend.
 */
public record VentaRequest(

        /** Opcional: si no se envía, el backend usa la fecha/hora del servidor. */
        LocalDateTime fecha,

        @NotNull UUID clienteId,

        @NotEmpty(message = "La venta debe tener al menos un ítem")
        @Valid
        List<VentaItemRequest> items,

        @NotNull @PositiveOrZero BigDecimal total,

        @NotNull MetodoPago metodoPago,

        @NotBlank String vendedor
) {
}
