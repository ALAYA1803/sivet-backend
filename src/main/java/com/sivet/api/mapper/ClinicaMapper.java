package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.dto.request.ClinicaPatchRequest;
import com.sivet.api.dto.request.ClinicaRequest;
import com.sivet.api.dto.response.ClinicaResponse;
import com.sivet.api.dto.response.ClinicaResumenResponse;
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

    /** Aplica solo los campos informados (no null) sobre una clínica existente (PATCH). */
    public void patchEntity(Clinica c, ClinicaPatchRequest req) {
        if (req.nombre() != null) {
            c.setNombre(req.nombre());
        }
        if (req.ruc() != null) {
            c.setRuc(req.ruc());
        }
        if (req.telefono() != null) {
            c.setTelefono(req.telefono());
        }
        if (req.email() != null) {
            c.setEmail(req.email());
        }
        if (req.direccion() != null) {
            c.setDireccion(req.direccion());
        }
    }

    public ClinicaResumenResponse toResumenResponse(Clinica c) {
        return new ClinicaResumenResponse(
                c.getId(),
                c.getNombre(),
                c.getRuc(),
                c.getSede(),
                c.getTelefono(),
                c.getEmail()
        );
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
