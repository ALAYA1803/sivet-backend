package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Atencion;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.domain.entity.Receta;
import com.sivet.api.dto.request.AtencionRequest;
import com.sivet.api.dto.response.AtencionResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.AtencionMapper;
import com.sivet.api.mapper.RecetaMapper;
import com.sivet.api.repository.AtencionRepository;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.MascotaRepository;
import com.sivet.api.repository.RecetaRepository;
import com.sivet.api.service.AtencionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtencionServiceImpl implements AtencionService {

    private final AtencionRepository atencionRepository;
    private final MascotaRepository mascotaRepository;
    private final RecetaRepository recetaRepository;
    private final ClinicaRepository clinicaRepository;
    private final AtencionMapper atencionMapper;
    private final RecetaMapper recetaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponse> listar(UUID clinicaId) {
        return atencionRepository.findByClinica_Id(clinicaId).stream()
                .map(atencionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponse> listarPorMascota(UUID clinicaId, UUID mascotaId) {
        if (mascotaId == null) {
            return listar(clinicaId);
        }
        return atencionRepository
                .findByClinica_IdAndMascota_IdOrderByFechaDesc(clinicaId, mascotaId).stream()
                .map(atencionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AtencionResponse obtener(UUID clinicaId, UUID id) {
        return atencionMapper.toResponse(getOrThrow(clinicaId, id));
    }

    /**
     * Registra la atención y, si corresponde, su receta de forma atómica (§3.4).
     * <ul>
     *   <li>Si trae {@code receta} embebida ⇒ crea ambas y las enlaza
     *       bidireccionalmente en la misma transacción.</li>
     *   <li>Si trae {@code recetaId} ⇒ enlaza una receta existente del tenant.</li>
     *   <li>Si no trae ninguna ⇒ crea solo la atención.</li>
     * </ul>
     */
    @Override
    @Transactional
    public AtencionResponse registrar(UUID clinicaId, AtencionRequest request) {
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);
        Mascota mascota = mascotaRepository.findByIdAndClinica_Id(request.mascotaId(), clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Mascota", request.mascotaId()));

        Atencion atencion = atencionMapper.toEntity(request, mascota, clinica);
        Atencion guardada = atencionRepository.save(atencion); // primero la atención (genera id)

        if (request.receta() != null) {
            crearRecetaEmbebida(clinica, guardada, request);
        } else if (request.recetaId() != null) {
            enlazarRecetaExistente(clinicaId, guardada, request.recetaId());
        }

        return atencionMapper.toResponse(guardada);
    }

    /** Crea la receta embebida y la enlaza 1:1 con la atención (misma transacción). */
    private void crearRecetaEmbebida(Clinica clinica, Atencion atencion, AtencionRequest request) {
        var items = request.receta().items();
        if (items == null || items.isEmpty()) {
            throw new BusinessException("La receta embebida debe tener al menos un ítem");
        }
        Receta receta = new Receta();
        receta.setClinica(clinica);
        receta.setAtencion(atencion);
        receta.setItems(recetaMapper.toItemEntities(items));
        Receta guardada = recetaRepository.save(receta);

        atencion.setReceta(guardada);
        atencionRepository.save(atencion);
    }

    /** Enlaza una receta ya existente del tenant, manteniendo consistencia bidireccional. */
    private void enlazarRecetaExistente(UUID clinicaId, Atencion atencion, UUID recetaId) {
        Receta receta = recetaRepository.findByIdAndClinica_Id(recetaId, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Receta", recetaId));
        atencion.setReceta(receta);
        atencionRepository.save(atencion);
        receta.setAtencion(atencion);
        recetaRepository.save(receta);
    }

    private Atencion getOrThrow(UUID clinicaId, UUID id) {
        return atencionRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Atención", id));
    }
}
