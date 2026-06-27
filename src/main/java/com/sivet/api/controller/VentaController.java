package com.sivet.api.controller;

import com.sivet.api.dto.request.AnularVentaRequest;
import com.sivet.api.dto.request.VentaRequest;
import com.sivet.api.dto.response.VentaResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.VentaService;
import com.sivet.api.service.document.DocumentResult;
import com.sivet.api.service.document.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;
    private final DocumentService documentService;

    @GetMapping
    public List<VentaResponse> listar() {
        return ventaService.listar(SecurityUtils.currentTenantId());
    }

    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable UUID id) {
        return ventaService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponse registrar(@Valid @RequestBody VentaRequest request) {
        return ventaService.registrar(SecurityUtils.currentTenantId(), request);
    }

    /** Anulación: PATCH /ventas/{id} con { estado: 'anulada', motivoAnulacion }. */
    @PatchMapping("/{id}")
    public VentaResponse anular(@PathVariable UUID id, @Valid @RequestBody AnularVentaRequest request) {
        return ventaService.anular(SecurityUtils.currentTenantId(), id, request);
    }

    /** Comprobante PDF (ticket): GET /ventas/{id}/comprobante.pdf (§4.1). */
    @GetMapping("/{id}/comprobante.pdf")
    public ResponseEntity<byte[]> comprobante(@PathVariable UUID id) {
        DocumentResult doc = documentService.comprobanteVenta(SecurityUtils.currentTenantId(), id);
        return descargaPdf(doc);
    }

    private ResponseEntity<byte[]> descargaPdf(DocumentResult doc) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.filename() + "\"")
                .body(doc.content());
    }
}
