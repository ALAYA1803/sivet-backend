package com.sivet.api.controller;

import com.sivet.api.dto.request.ClinicaRequest;
import com.sivet.api.dto.response.ClinicaResponse;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.ClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Clínicas (tenants). El POST es público (onboarding del tenant). El GET por id
 * rehidrata la clínica tras el login (§2.1) y está exento del header X-Tenant-ID:
 * la pertenencia se valida contra el {@code veterinaria_id} del token.
 */
@RestController
@RequestMapping("/clinicas")
@RequiredArgsConstructor
public class ClinicaController {

    private final ClinicaService clinicaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicaResponse crear(@Valid @RequestBody ClinicaRequest request) {
        return clinicaService.crear(request);
    }

    @GetMapping("/{id}")
    public ClinicaResponse obtener(@PathVariable UUID id) {
        // Un usuario solo puede leer su propia clínica.
        if (!id.equals(SecurityUtils.currentUser().veterinariaId())) {
            throw ResourceNotFoundException.of("Clínica", id);
        }
        return clinicaService.obtener(id);
    }
}
