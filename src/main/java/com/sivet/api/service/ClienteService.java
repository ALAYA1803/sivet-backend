package com.sivet.api.service;

import com.sivet.api.dto.request.ClienteRequest;
import com.sivet.api.dto.response.ClienteResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de clientes (dueños), aislada por tenant.
 */
public interface ClienteService {

    List<ClienteResponse> listar(UUID clinicaId);

    ClienteResponse obtener(UUID clinicaId, UUID id);

    ClienteResponse crear(UUID clinicaId, ClienteRequest request);

    ClienteResponse actualizar(UUID clinicaId, UUID id, ClienteRequest request);

    void eliminar(UUID clinicaId, UUID id);
}
