package com.sivet.api.dto.request;

import com.sivet.api.domain.enums.CategoriaProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Datos de entrada para crear/actualizar un producto. El tenant lo estampa el backend.
 * <p>{@code stock}/{@code stockMin} son null para categoría 'Servicio' (la regla de
 * integridad se valida en la capa de servicio).
 */
public record ProductoRequest(

        @NotBlank String codigo,

        @NotBlank String nombre,

        @NotNull CategoriaProducto categoria,

        @NotNull @PositiveOrZero BigDecimal precio,

        @PositiveOrZero Integer stock,

        @PositiveOrZero Integer stockMin,

        @NotBlank String unidad
) {
}
