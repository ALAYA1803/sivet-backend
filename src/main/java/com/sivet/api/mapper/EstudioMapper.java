package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Estudio;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.dto.request.EstudioRequest;
import com.sivet.api.dto.response.EstudioResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre {@link Estudio} y sus DTOs (reporte de texto, sin archivos).
 */
@Component
public class EstudioMapper {

    public Estudio toEntity(EstudioRequest req, Mascota mascota, Clinica clinica) {
        Estudio e = new Estudio();
        e.setMascota(mascota);
        e.setTitulo(req.titulo());
        e.setTag(req.tag());
        e.setFecha(req.fecha());
        e.setVeterinario(req.veterinario());
        e.setClinica(clinica);
        return e;
    }

    public EstudioResponse toResponse(Estudio e) {
        return new EstudioResponse(
                e.getId(),
                e.getMascota().getId(),
                e.getTitulo(),
                e.getTag(),
                e.getFecha(),
                e.getVeterinario()
        );
    }
}
