package com.sivet.api.service;

import com.sivet.api.dto.request.AnularVentaRequest;
import com.sivet.api.dto.request.VentaRequest;
import com.sivet.api.dto.response.VentaResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de ventas (POS), aislada por tenant. Coordina de forma atómica el
 * descuento/restauración de inventario (§3.2).
 */
public interface VentaService {

    List<VentaResponse> listar(UUID clinicaId);

    VentaResponse obtener(UUID clinicaId, UUID id);

    /** Registra una venta 'completada' y descuenta stock en la misma transacción. */
    VentaResponse registrar(UUID clinicaId, VentaRequest request);

    /** Anula una venta (idempotente) y restaura stock en la misma transacción. */
    VentaResponse anular(UUID clinicaId, UUID id, AnularVentaRequest request);
}
