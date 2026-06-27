package com.sivet.api.dto.response;

import java.util.UUID;

/**
 * Vista resumida de una clínica para el listado del SUPERADMIN en el backoffice.
 */
public record ClinicaResumenResponse(
        UUID id,
        String nombre,
        String ruc,
        String sede,
        String telefono,
        String email
) {
}
