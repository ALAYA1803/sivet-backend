package com.sivet.api.service;

import com.sivet.api.dto.request.EstudioRequest;
import com.sivet.api.dto.response.EstudioResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de estudios complementarios (reportes de texto), aislada por tenant.
 */
public interface EstudioService {

    List<EstudioResponse> listar(UUID clinicaId);

    /** Filtro opcional por mascota (§2.11). Si {@code mascotaId} es null, lista todos. */
    List<EstudioResponse> listarPorMascota(UUID clinicaId, UUID mascotaId);

    EstudioResponse obtener(UUID clinicaId, UUID id);

    EstudioResponse crear(UUID clinicaId, EstudioRequest request);
}
