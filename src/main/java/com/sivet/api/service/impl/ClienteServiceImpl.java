package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.dto.request.ClienteRequest;
import com.sivet.api.dto.response.ClienteResponse;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.ClienteMapper;
import com.sivet.api.repository.ClienteRepository;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClinicaRepository clinicaRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar(UUID clinicaId) {
        return clienteRepository.findByClinica_Id(clinicaId).stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtener(UUID clinicaId, UUID id) {
        return clienteMapper.toResponse(getOrThrow(clinicaId, id));
    }

    @Override
    @Transactional
    public ClienteResponse crear(UUID clinicaId, ClienteRequest request) {
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId); // estampa tenant
        Cliente cliente = clienteMapper.toEntity(request, clinica);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(UUID clinicaId, UUID id, ClienteRequest request) {
        Cliente cliente = getOrThrow(clinicaId, id);
        clienteMapper.updateEntity(cliente, request);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void eliminar(UUID clinicaId, UUID id) {
        clienteRepository.delete(getOrThrow(clinicaId, id));
    }

    private Cliente getOrThrow(UUID clinicaId, UUID id) {
        return clienteRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", id));
    }
}
