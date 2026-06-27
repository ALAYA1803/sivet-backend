package com.sivet.api.service;

import com.sivet.api.dto.request.ProductoRequest;
import com.sivet.api.dto.request.StockPatchRequest;
import com.sivet.api.dto.response.ProductoResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión del catálogo/inventario, aislada por tenant. Aplica la regla de integridad
 * de inventario (Servicio ⇒ stock/stockMin null) §1.7.
 */
public interface ProductoService {

    List<ProductoResponse> listar(UUID clinicaId);

    ProductoResponse obtener(UUID clinicaId, UUID id);

    ProductoResponse crear(UUID clinicaId, ProductoRequest request);

    ProductoResponse actualizar(UUID clinicaId, UUID id, ProductoRequest request);

    /** Ajuste directo de stock (PATCH /productos/{id}). */
    ProductoResponse ajustarStock(UUID clinicaId, UUID id, StockPatchRequest request);
}
