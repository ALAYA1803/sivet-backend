package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.dto.request.ClienteRequest;
import com.sivet.api.dto.response.ClienteResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre {@link Cliente} y sus DTOs. El tenant lo provee el servicio.
 */
@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest req, Clinica clinica) {
        Cliente c = new Cliente();
        c.setNombre(req.nombre());
        c.setDni(req.dni());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        c.setDireccion(req.direccion());
        c.setClinica(clinica);
        return c;
    }

    public void updateEntity(Cliente c, ClienteRequest req) {
        c.setNombre(req.nombre());
        c.setDni(req.dni());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        c.setDireccion(req.direccion());
    }

    public ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNombre(),
                c.getDni(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion()
        );
    }
}
