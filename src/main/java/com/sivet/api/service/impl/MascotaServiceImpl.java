package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.dto.request.MascotaRequest;
import com.sivet.api.dto.response.MascotaResponse;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.MascotaMapper;
import com.sivet.api.repository.ClienteRepository;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.MascotaRepository;
import com.sivet.api.service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;
    private final ClienteRepository clienteRepository;
    private final ClinicaRepository clinicaRepository;
    private final MascotaMapper mascotaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MascotaResponse> listar(UUID clinicaId) {
        return mascotaRepository.findByClinica_Id(clinicaId).stream()
                .map(mascotaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MascotaResponse> listarPorCliente(UUID clinicaId, UUID clienteId) {
        if (clienteId == null) {
            return listar(clinicaId);
        }
        return mascotaRepository.findByClinica_IdAndCliente_Id(clinicaId, clienteId).stream()
                .map(mascotaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MascotaResponse obtener(UUID clinicaId, UUID id) {
        return mascotaMapper.toResponse(getOrThrow(clinicaId, id));
    }

    @Override
    @Transactional
    public MascotaResponse crear(UUID clinicaId, MascotaRequest request) {
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);
        Cliente cliente = resolverCliente(clinicaId, request.clienteId());
        Mascota mascota = mascotaMapper.toEntity(request, cliente, clinica);
        return mascotaMapper.toResponse(mascotaRepository.save(mascota));
    }

    @Override
    @Transactional
    public MascotaResponse actualizar(UUID clinicaId, UUID id, MascotaRequest request) {
        Mascota mascota = getOrThrow(clinicaId, id);
        Cliente cliente = resolverCliente(clinicaId, request.clienteId());
        mascota.setNombre(request.nombre());
        mascota.setEspecie(request.especie());
        mascota.setRaza(request.raza());
        mascota.setSexo(request.sexo());
        mascota.setEdad(request.edad());
        mascota.setPeso(request.peso());
        mascota.setColor(request.color());
        mascota.setFoto(request.foto());
        mascota.setEsterilizada(Boolean.TRUE.equals(request.esterilizada()));
        mascota.setMicrochip(request.microchip());
        mascota.setVacunacion(request.vacunacion());
        mascota.setAlergias(request.alergias());
        mascota.setNotas(request.notas());
        mascota.setCliente(cliente);
        return mascotaMapper.toResponse(mascotaRepository.save(mascota));
    }

    @Override
    @Transactional
    public void eliminar(UUID clinicaId, UUID id) {
        mascotaRepository.delete(getOrThrow(clinicaId, id));
    }

    private Cliente resolverCliente(UUID clinicaId, UUID clienteId) {
        return clienteRepository.findByIdAndClinica_Id(clienteId, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", clienteId));
    }

    private Mascota getOrThrow(UUID clinicaId, UUID id) {
        return mascotaRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Mascota", id));
    }
}
