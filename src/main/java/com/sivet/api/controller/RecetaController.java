package com.sivet.api.controller;

import com.sivet.api.dto.request.RecetaRequest;
import com.sivet.api.dto.response.RecetaResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.RecetaService;
import com.sivet.api.service.document.DocumentResult;
import com.sivet.api.service.document.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;
    private final DocumentService documentService;

    @GetMapping
    public List<RecetaResponse> listar() {
        return recetaService.listar(SecurityUtils.currentTenantId());
    }

    @GetMapping("/{id}")
    public RecetaResponse obtener(@PathVariable UUID id) {
        return recetaService.obtener(SecurityUtils.currentTenantId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecetaResponse crear(@Valid @RequestBody RecetaRequest request) {
        return recetaService.crear(SecurityUtils.currentTenantId(), request);
    }

    /** Receta médica en PDF: GET /recetas/{id}/pdf (§4.2). */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable UUID id) {
        DocumentResult doc = documentService.recetaPdf(SecurityUtils.currentTenantId(), id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.filename() + "\"")
                .body(doc.content());
    }
}
