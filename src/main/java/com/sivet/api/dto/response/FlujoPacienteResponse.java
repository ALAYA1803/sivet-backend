package com.sivet.api.dto.response;

/**
 * Read-model del dashboard: flujo de pacientes por día. Se calcula on-the-fly
 * por tenant (no es una entidad editable).
 */
public record FlujoPacienteResponse(
        String dia,
        long total
) {
}
