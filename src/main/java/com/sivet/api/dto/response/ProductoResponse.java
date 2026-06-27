package com.sivet.api.dto.response;

import com.sivet.api.domain.enums.CategoriaProducto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representación de salida de un producto. {@code stock}/{@code stockMin} pueden
 * ser null (categoría 'Servicio').
 */
public record ProductoResponse(
        UUID id,
        String codigo,
        String nombre,
        CategoriaProducto categoria,
        BigDecimal precio,
        Integer stock,
        Integer stockMin,
        String unidad
) {
}
