package com.sivet.api.controller;

import com.sivet.api.dto.request.AtencionRequest;
import com.sivet.api.dto.response.AtencionResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.AtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Atenciones (historia clínica): lectura, registro (con creación atómica de receta
 * vinculada, §3.4) y eliminación administrativa (solo ADMIN_CLINICA) aislada por tenant.
 */
@RestController
@RequestMapping("/atenciones")
@RequiredArgsConstructor
public class AtencionController {

    private final AtencionService atencionService;

    /** Filtro opcional por mascota (orden desc por fecha): GET /atenciones?mascotaId=... (§2.11). */
    @GetMapping
    public List<AtencionResponse> listar(@RequestParam(required = false) UUID mascotaId) {
        return atencionService.listarPorMascota(SecurityUtils.currentTenantId(), mascotaId);
    }

    @GetMapping("/{id}")
    public AtencionResponse obtener(@PathVariable UUID id) {
        return atencionService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtencionResponse registrar(@Valid @RequestBody AtencionRequest request) {
        return atencionService.registrar(SecurityUtils.currentTenantId(), request);
    }

    /** Eliminar atención (aislada por tenant): solo el admin de la clínica. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_CLINICA')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        atencionService.eliminar(SecurityUtils.currentTenantId(), id);
    }
}
