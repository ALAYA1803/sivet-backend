package com.sivet.api.controller;

import com.sivet.api.dto.request.ClinicaOnboardingRequest;
import com.sivet.api.dto.response.ClinicaOnboardingResponse;
import com.sivet.api.dto.response.ClinicaResumenResponse;
import com.sivet.api.service.SuperadminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Backoffice B2B del dueño del SaaS. Todas las rutas exigen el rol SUPERADMIN; al estar
 * fuera de un tenant concreto, no requieren el header X-Tenant-ID (ver filtro de tenant).
 */
@RestController
@RequestMapping("/admin-sivet")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERADMIN')")
public class SuperadminController {

    private final SuperadminService superadminService;

    /** Alta de una clínica y su doctor principal; devuelve la contraseña temporal en claro. */
    @PostMapping("/clinicas-onboarding")
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicaOnboardingResponse onboarding(@Valid @RequestBody ClinicaOnboardingRequest request) {
        return superadminService.onboardingClinica(request);
    }

    /** Listado de todas las clínicas registradas en la plataforma. */
    @GetMapping("/clinicas")
    public List<ClinicaResumenResponse> listarClinicas() {
        return superadminService.listarClinicas();
    }
}
