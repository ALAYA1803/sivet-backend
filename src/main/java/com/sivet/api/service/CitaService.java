package com.sivet.api.service;

import com.sivet.api.domain.enums.EstadoCita;
import com.sivet.api.dto.request.CitaRequest;
import com.sivet.api.dto.response.CitaResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Gestión de la agenda de citas, aislada por tenant. Impone el invariante
 * anti-colisión de franjas (§3.3).
 */
public interface CitaService {

    List<CitaResponse> listar(UUID clinicaId);

    /** Filtro opcional por fecha (§2.11). Si {@code fecha} es null, lista todas. */
    List<CitaResponse> listarPorFecha(UUID clinicaId, LocalDate fecha);

    CitaResponse obtener(UUID clinicaId, UUID id);

    /** Agenda una cita 'pendiente'; rechaza colisiones de franja con 409. */
    CitaResponse agendar(UUID clinicaId, CitaRequest request);

    CitaResponse cambiarEstado(UUID clinicaId, UUID id, EstadoCita nuevoEstado);
}
