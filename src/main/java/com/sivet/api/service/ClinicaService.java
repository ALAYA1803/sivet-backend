package com.sivet.api.service;

import com.sivet.api.dto.request.ClinicaPatchRequest;
import com.sivet.api.dto.request.ClinicaRequest;
import com.sivet.api.dto.response.ClinicaResponse;

import java.util.UUID;

/**
 * Gestión de clínicas (tenants). No se filtra por tenant: la clínica ES el tenant.
 */
public interface ClinicaService {

    ClinicaResponse crear(ClinicaRequest request);

    ClinicaResponse obtener(UUID id);

    ClinicaResponse actualizar(UUID id, ClinicaRequest request);

    /** Actualización parcial (PATCH): solo aplica los campos informados. */
    ClinicaResponse actualizarParcial(UUID id, ClinicaPatchRequest request);
}
