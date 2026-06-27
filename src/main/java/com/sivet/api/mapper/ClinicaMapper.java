package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.dto.request.ClinicaRequest;
import com.sivet.api.dto.response.ClinicaResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre {@link Clinica} y sus DTOs.
 */
@Component
public class ClinicaMapper {

    public Clinica toEntity(ClinicaRequest req) {
        Clinica c = new Clinica();
        c.setNombre(req.nombre());
        c.setSede(req.sede());
        c.setRuc(req.ruc());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        c.setDireccion(req.direccion());
        return c;
    }

    /** Copia los campos editables sobre una clínica existente (PUT). */
    public void updateEntity(Clinica c, ClinicaRequest req) {
        c.setNombre(req.nombre());
        c.setSede(req.sede());
        c.setRuc(req.ruc());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        c.setDireccion(req.direccion());
    }

    public ClinicaResponse toResponse(Clinica c) {
        return new ClinicaResponse(
                c.getId(),
                c.getNombre(),
                c.getSede(),
                c.getRuc(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion()
        );
    }
}
