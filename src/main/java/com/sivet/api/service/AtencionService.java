package com.sivet.api.service;

import com.sivet.api.dto.request.AtencionRequest;
import com.sivet.api.dto.response.AtencionResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de atenciones (historia clínica), aislada por tenant. La atención no se
 * edita (sin update); admite eliminación administrativa. Registra atención + receta
 * de forma atómica (§3.4).
 */
public interface AtencionService {

    List<AtencionResponse> listar(UUID clinicaId);

    /** Filtro opcional por mascota, orden desc por fecha (§2.11). */
    List<AtencionResponse> listarPorMascota(UUID clinicaId, UUID mascotaId);

    AtencionResponse obtener(UUID clinicaId, UUID id);

    /**
     * Registra una atención. Si trae {@code receta} embebida, crea atención + receta
     * en una sola transacción y las enlaza. Si trae {@code recetaId}, enlaza una
     * receta existente del tenant.
     */
    AtencionResponse registrar(UUID clinicaId, AtencionRequest request);

    /**
     * Elimina una atención del tenant (y su receta vinculada, si la tiene).
     * Valida que la atención pertenezca a la clínica indicada (aislamiento).
     */
    void eliminar(UUID clinicaId, UUID id);
}
