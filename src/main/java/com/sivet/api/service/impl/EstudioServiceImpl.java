package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Estudio;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.dto.request.EstudioRequest;
import com.sivet.api.dto.response.EstudioResponse;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.EstudioMapper;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.EstudioRepository;
import com.sivet.api.repository.MascotaRepository;
import com.sivet.api.service.EstudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EstudioServiceImpl implements EstudioService {

    private final EstudioRepository estudioRepository;
    private final MascotaRepository mascotaRepository;
    private final ClinicaRepository clinicaRepository;
    private final EstudioMapper estudioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EstudioResponse> listar(UUID clinicaId) {
        return estudioRepository.findByClinica_Id(clinicaId).stream()
                .map(estudioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudioResponse> listarPorMascota(UUID clinicaId, UUID mascotaId) {
        if (mascotaId == null) {
            return listar(clinicaId);
        }
        return estudioRepository.findByClinica_IdAndMascota_Id(clinicaId, mascotaId).stream()
                .map(estudioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EstudioResponse obtener(UUID clinicaId, UUID id) {
        Estudio estudio = estudioRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Estudio", id));
        return estudioMapper.toResponse(estudio);
    }

    @Override
    @Transactional
    public EstudioResponse crear(UUID clinicaId, EstudioRequest request) {
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);
        Mascota mascota = mascotaRepository.findByIdAndClinica_Id(request.mascotaId(), clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Mascota", request.mascotaId()));
        Estudio estudio = estudioMapper.toEntity(request, mascota, clinica);
        return estudioMapper.toResponse(estudioRepository.save(estudio));
    }
}
