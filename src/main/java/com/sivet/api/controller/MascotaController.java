package com.sivet.api.controller;

import com.sivet.api.dto.request.MascotaRequest;
import com.sivet.api.dto.response.MascotaResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    /** Filtro opcional por dueño: GET /mascotas?clienteId=... (§2.11). */
    @GetMapping
    public List<MascotaResponse> listar(@RequestParam(required = false) UUID clienteId) {
        return mascotaService.listarPorCliente(SecurityUtils.currentTenantId(), clienteId);
    }

    @GetMapping("/{id}")
    public MascotaResponse obtener(@PathVariable UUID id) {
        return mascotaService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MascotaResponse crear(@Valid @RequestBody MascotaRequest request) {
        return mascotaService.crear(SecurityUtils.currentTenantId(), request);
    }

    @PutMapping("/{id}")
    public MascotaResponse actualizar(@PathVariable UUID id, @Valid @RequestBody MascotaRequest request) {
        return mascotaService.actualizar(SecurityUtils.currentTenantId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        mascotaService.eliminar(SecurityUtils.currentTenantId(), id);
    }
}
