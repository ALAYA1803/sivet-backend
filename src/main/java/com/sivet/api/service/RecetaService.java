package com.sivet.api.service;

import com.sivet.api.dto.request.RecetaRequest;
import com.sivet.api.dto.response.RecetaResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de recetas, aislada por tenant. Soporta el flujo encadenado del frontend
 * (POST /recetas antes/después de la atención). La creación atómica embebida vive en
 * {@link AtencionService}.
 */
public interface RecetaService {

    List<RecetaResponse> listar(UUID clinicaId);

    RecetaResponse obtener(UUID clinicaId, UUID id);

    /** Crea una receta (≥1 ítem). Si la atención referenciada ya existe, las enlaza. */
    RecetaResponse crear(UUID clinicaId, RecetaRequest request);
}
