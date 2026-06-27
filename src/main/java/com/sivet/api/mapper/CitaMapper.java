package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Cita;
import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.dto.request.CitaRequest;
import com.sivet.api.dto.response.CitaResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre {@link Cita} y sus DTOs. Mascota, cliente, tenant y estado inicial
 * los resuelve el servicio (estado = 'pendiente' al crear).
 */
@Component
public class CitaMapper {

    public Cita toEntity(CitaRequest req, Mascota mascota, Cliente cliente, Clinica clinica) {
        Cita c = new Cita();
        c.setMascota(mascota);
        c.setCliente(cliente);
        c.setFecha(req.fecha());
        c.setHora(req.hora());
        c.setMotivo(req.motivo());
        c.setClinica(clinica);
        return c;
    }

    public CitaResponse toResponse(Cita c) {
        return new CitaResponse(
                c.getId(),
                c.getMascota().getId(),
                c.getCliente().getId(),
                c.getFecha(),
                c.getHora(),
                c.getMotivo(),
                c.getEstado()
        );
    }
}
