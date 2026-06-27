package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Atencion;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.dto.request.AtencionRequest;
import com.sivet.api.dto.response.AtencionResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo de {@link Atencion}. Mascota, receta y tenant los resuelve el servicio
 * (la receta se enlaza dentro de la transacción atómica §3.4).
 */
@Component
public class AtencionMapper {

    public Atencion toEntity(AtencionRequest req, Mascota mascota, Clinica clinica) {
        Atencion a = new Atencion();
        a.setMascota(mascota);
        a.setFecha(req.fecha());
        a.setTipo(req.tipo());
        a.setMotivo(req.motivo());
        a.setDiagnostico(req.diagnostico());
        a.setTratamiento(req.tratamiento());
        a.setVeterinario(req.veterinario());
        a.setTemperatura(req.temperatura());
        a.setFrecCardiaca(req.frecCardiaca());
        a.setFrecRespiratoria(req.frecRespiratoria());
        a.setClinica(clinica);
        return a;
    }

    public AtencionResponse toResponse(Atencion a) {
        return new AtencionResponse(
                a.getId(),
                a.getMascota().getId(),
                a.getFecha(),
                a.getTipo(),
                a.getMotivo(),
                a.getDiagnostico(),
                a.getTratamiento(),
                a.getVeterinario(),
                a.getTemperatura(),
                a.getFrecCardiaca(),
                a.getFrecRespiratoria(),
                a.getReceta() != null ? a.getReceta().getId() : null
        );
    }
}
