package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Atencion;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Receta;
import com.sivet.api.dto.request.RecetaRequest;
import com.sivet.api.dto.response.RecetaResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.RecetaMapper;
import com.sivet.api.repository.AtencionRepository;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.RecetaRepository;
import com.sivet.api.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final AtencionRepository atencionRepository;
    private final ClinicaRepository clinicaRepository;
    private final RecetaMapper recetaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponse> listar(UUID clinicaId) {
        return recetaRepository.findByClinica_Id(clinicaId).stream()
                .map(recetaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecetaResponse obtener(UUID clinicaId, UUID id) {
        Receta receta = recetaRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Receta", id));
        return recetaMapper.toResponse(receta);
    }

    /**
     * Crea una receta vinculada a una atención (flujo encadenado del frontend, §3.4).
     * <p>El frontend puede hacer POST /recetas <em>antes</em> de crear la atención
     * (envía un atencionId tentativo). Por eso, si la atención aún no existe en el
     * tenant, la receta se crea sin enlazar y la atención la referenciará al crearse
     * con {@code recetaId}. Si la atención ya existe, se enlazan bidireccionalmente.
     */
    @Override
    @Transactional
    public RecetaResponse crear(UUID clinicaId, RecetaRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new BusinessException("La receta debe tener al menos un ítem");
        }
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);

        Receta receta = new Receta();
        receta.setClinica(clinica);
        receta.setItems(recetaMapper.toItemEntities(request.items()));

        Atencion atencion = atencionRepository
                .findByIdAndClinica_Id(request.atencionId(), clinicaId)
                .orElse(null);
        if (atencion != null) {
            receta.setAtencion(atencion);
        }

        Receta guardada = recetaRepository.save(receta);

        // Mantener consistencia bidireccional cuando la atención ya existe (§3.4).
        if (atencion != null) {
            atencion.setReceta(guardada);
            atencionRepository.save(atencion);
        }

        return recetaMapper.toResponse(guardada);
    }
}
