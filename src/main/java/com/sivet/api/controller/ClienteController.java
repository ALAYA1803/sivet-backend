package com.sivet.api.controller;

import com.sivet.api.dto.request.ClienteRequest;
import com.sivet.api.dto.response.ClienteResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.ClienteService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar(SecurityUtils.currentTenantId());
    }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable UUID id) {
        return clienteService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody ClienteRequest request) {
        return clienteService.crear(SecurityUtils.currentTenantId(), request);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        return clienteService.actualizar(SecurityUtils.currentTenantId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        clienteService.eliminar(SecurityUtils.currentTenantId(), id);
    }
}
