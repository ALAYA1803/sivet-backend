package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Usuario;
import com.sivet.api.dto.response.UsuarioResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo de {@link Usuario} hacia su DTO de salida. La construcción de la entidad
 * (con hash de password y tenant) la realiza el servicio, no el mapper.
 */
@Component
public class UsuarioMapper {

    /** Nunca expone el password ni su hash. */
    public UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getUsername(),
                u.getNombre(),
                u.getRol(),
                u.isRequiereCambioPassword(),
                u.getClinica().getId()
        );
    }
}
