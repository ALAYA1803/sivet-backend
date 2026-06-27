package com.sivet.api.service;

import com.sivet.api.dto.request.ClinicaOnboardingRequest;
import com.sivet.api.dto.response.ClinicaOnboardingResponse;
import com.sivet.api.dto.response.ClinicaResumenResponse;

import java.util.List;

/**
 * Backoffice B2B del dueño del SaaS (SUPERADMIN). Da de alta clínicas y a su doctor
 * principal en una sola transacción atómica.
 */
public interface SuperadminService {

    /**
     * Crea la clínica y su usuario ADMIN_CLINICA con una contraseña temporal generada
     * por el backend. Devuelve esa clave en claro (única vez) para mostrarla en el front.
     */
    ClinicaOnboardingResponse onboardingClinica(ClinicaOnboardingRequest request);

    /** Todas las clínicas registradas en la plataforma (vista del SUPERADMIN). */
    List<ClinicaResumenResponse> listarClinicas();
}
