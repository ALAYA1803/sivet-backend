package com.sivet.api.controller;

import com.sivet.api.dto.request.EstudioRequest;
import com.sivet.api.dto.response.EstudioResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.EstudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
 * Estudios complementarios (reportes de texto, sin archivos). El frontend hoy solo
 * lee; se prevé el POST para registrar reportes (§2.6).
 */
@RestController
@RequestMapping("/estudios")
@RequiredArgsConstructor
public class EstudioController {

    private final EstudioService estudioService;

    /** Filtro opcional por mascota: GET /estudios?mascotaId=... (§2.11). */
    @GetMapping
    public List<EstudioResponse> listar(@RequestParam(required = false) UUID mascotaId) {
        return estudioService.listarPorMascota(SecurityUtils.currentTenantId(), mascotaId);
    }

    @GetMapping("/{id}")
    public EstudioResponse obtener(@PathVariable UUID id) {
        return estudioService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudioResponse crear(@Valid @RequestBody EstudioRequest request) {
        return estudioService.crear(SecurityUtils.currentTenantId(), request);
    }
}
