package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Cita;
import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.domain.enums.EstadoCita;
import com.sivet.api.dto.request.CitaRequest;
import com.sivet.api.dto.response.CitaResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ConflictException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.CitaMapper;
import com.sivet.api.repository.CitaRepository;
import com.sivet.api.repository.ClienteRepository;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.MascotaRepository;
import com.sivet.api.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final ClienteRepository clienteRepository;
    private final ClinicaRepository clinicaRepository;
    private final CitaMapper citaMapper;

    /** Franjas válidas de 30 min: 09:00, 09:30, …, 18:00 (§3.3). */
    private static final Set<String> FRANJAS_VALIDAS = construirFranjas();

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar(UUID clinicaId) {
        return citaRepository.findByClinica_Id(clinicaId).stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarPorFecha(UUID clinicaId, LocalDate fecha) {
        if (fecha == null) {
            return listar(clinicaId);
        }
        return citaRepository.findByClinica_IdAndFecha(clinicaId, fecha).stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtener(UUID clinicaId, UUID id) {
        return citaMapper.toResponse(getOrThrow(clinicaId, id));
    }

    /**
     * Agenda una cita 'pendiente'. Impone el invariante anti-colisión (§3.3): una
     * franja (clínica, fecha, hora) está ocupada si existe una cita con estado
     * distinto de 'cancelada'. Las canceladas liberan la franja.
     */
    @Override
    @Transactional
    public CitaResponse agendar(UUID clinicaId, CitaRequest request) {
        if (!FRANJAS_VALIDAS.contains(request.hora())) {
            throw new BusinessException(
                    "Hora fuera de las franjas válidas (09:00–18:00, bloques de 30 min): " + request.hora());
        }
        // No se puede agendar en el pasado: si la cita es para hoy, la hora no puede haber pasado.
        if (request.fecha().isEqual(LocalDate.now())
                && LocalTime.parse(request.hora()).isBefore(LocalTime.now())) {
            throw new BusinessException(
                    "No se puede agendar una cita en el pasado: la hora " + request.hora()
                            + " ya pasó para hoy");
        }
        boolean ocupada = citaRepository.existsByClinica_IdAndFechaAndHoraAndEstadoNot(
                clinicaId, request.fecha(), request.hora(), EstadoCita.CANCELADA);
        if (ocupada) {
            throw new ConflictException(
                    "La franja " + request.fecha() + " " + request.hora() + " ya está ocupada");
        }

        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);
        Mascota mascota = mascotaRepository.findByIdAndClinica_Id(request.mascotaId(), clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Mascota", request.mascotaId()));
        Cliente cliente = clienteRepository.findByIdAndClinica_Id(request.clienteId(), clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));

        Cita cita = citaMapper.toEntity(request, mascota, cliente, clinica);
        cita.setEstado(EstadoCita.PENDIENTE); // al crear, siempre 'pendiente'
        return citaMapper.toResponse(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public CitaResponse cambiarEstado(UUID clinicaId, UUID id, EstadoCita nuevoEstado) {
        Cita cita = getOrThrow(clinicaId, id);
        cita.setEstado(nuevoEstado);
        return citaMapper.toResponse(citaRepository.save(cita));
    }

    private Cita getOrThrow(UUID clinicaId, UUID id) {
        return citaRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Cita", id));
    }

    private static Set<String> construirFranjas() {
        Set<String> franjas = new HashSet<>();
        for (int hora = 9; hora <= 18; hora++) {
            franjas.add(String.format("%02d:00", hora));
            if (hora < 18) {
                franjas.add(String.format("%02d:30", hora));
            }
        }
        return franjas; // 09:00 … 18:00 inclusive
    }
}
