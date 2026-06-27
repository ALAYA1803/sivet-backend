package com.sivet.api.controller;

import com.sivet.api.dto.request.CitaRequest;
import com.sivet.api.dto.request.EstadoCitaPatchRequest;
import com.sivet.api.dto.response.CitaResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    /** Filtro opcional por fecha: GET /citas?fecha=YYYY-MM-DD (§2.11). */
    @GetMapping
    public List<CitaResponse> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return citaService.listarPorFecha(SecurityUtils.currentTenantId(), fecha);
    }

    @GetMapping("/{id}")
    public CitaResponse obtener(@PathVariable UUID id) {
        return citaService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaResponse agendar(@Valid @RequestBody CitaRequest request) {
        return citaService.agendar(SecurityUtils.currentTenantId(), request);
    }

    /** Cambio de estado: PATCH /citas/{id} con { estado }. */
    @PatchMapping("/{id}")
    public CitaResponse cambiarEstado(@PathVariable UUID id, @Valid @RequestBody EstadoCitaPatchRequest request) {
        return citaService.cambiarEstado(SecurityUtils.currentTenantId(), id, request.estado());
    }
}
