package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.dto.request.ClinicaRequest;
import com.sivet.api.dto.response.ClinicaResponse;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.ClinicaMapper;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.service.ClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicaServiceImpl implements ClinicaService {

    private final ClinicaRepository clinicaRepository;
    private final ClinicaMapper clinicaMapper;

    @Override
    @Transactional
    public ClinicaResponse crear(ClinicaRequest request) {
        Clinica clinica = clinicaMapper.toEntity(request);
        return clinicaMapper.toResponse(clinicaRepository.save(clinica));
    }

    @Override
    @Transactional(readOnly = true)
    public ClinicaResponse obtener(UUID id) {
        return clinicaMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public ClinicaResponse actualizar(UUID id, ClinicaRequest request) {
        Clinica clinica = getOrThrow(id);
        clinicaMapper.updateEntity(clinica, request);
        return clinicaMapper.toResponse(clinicaRepository.save(clinica));
    }

    private Clinica getOrThrow(UUID id) {
        return clinicaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Clínica", id));
    }
}
