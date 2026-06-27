package com.sivet.api.service;

import com.sivet.api.dto.request.MascotaRequest;
import com.sivet.api.dto.response.MascotaResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de mascotas (pacientes), aislada por tenant.
 */
public interface MascotaService {

    List<MascotaResponse> listar(UUID clinicaId);

    /** Filtro opcional por dueño (§2.11). Si {@code clienteId} es null, lista todas. */
    List<MascotaResponse> listarPorCliente(UUID clinicaId, UUID clienteId);

    MascotaResponse obtener(UUID clinicaId, UUID id);

    MascotaResponse crear(UUID clinicaId, MascotaRequest request);

    MascotaResponse actualizar(UUID clinicaId, UUID id, MascotaRequest request);

    void eliminar(UUID clinicaId, UUID id);
}
