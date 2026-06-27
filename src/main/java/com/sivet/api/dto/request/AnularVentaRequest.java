package com.sivet.api.dto.request;

import com.sivet.api.domain.enums.EstadoVenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo del PATCH /ventas/{id} para anular una venta.
 * Restaura inventario en la misma transacción (capa de servicio).
 */
public record AnularVentaRequest(

        @NotNull EstadoVenta estado,

        @NotBlank String motivoAnulacion
) {
}
