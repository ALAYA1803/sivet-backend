package com.sivet.api.dto.response;

/**
 * Read-model del dashboard: KPI "Vendidos hoy". Se calcula on-the-fly por tenant
 * sobre las ventas completadas del día actual (no es una entidad editable).
 *
 * @param unidadesVendidas suma de {@code cantidad} de los ítems vendidos hoy
 * @param numeroVentas     número de ventas completadas hoy
 */
public record VendidosHoyResponse(
        long unidadesVendidas,
        long numeroVentas
) {
}
