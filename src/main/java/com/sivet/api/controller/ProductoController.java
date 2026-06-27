package com.sivet.api.controller;

import com.sivet.api.dto.request.ProductoRequest;
import com.sivet.api.dto.request.StockPatchRequest;
import com.sivet.api.dto.response.ProductoResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoService.listar(SecurityUtils.currentTenantId());
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable UUID id) {
        return productoService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) {
        return productoService.crear(SecurityUtils.currentTenantId(), request);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable UUID id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizar(SecurityUtils.currentTenantId(), id, request);
    }

    /** Ajuste de inventario: PATCH /productos/{id} con { stock }. */
    @PatchMapping("/{id}")
    public ProductoResponse ajustarStock(@PathVariable UUID id, @Valid @RequestBody StockPatchRequest request) {
        return productoService.ajustarStock(SecurityUtils.currentTenantId(), id, request);
    }
}
