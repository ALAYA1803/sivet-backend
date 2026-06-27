package com.sivet.api.controller;

import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.document.DocumentResult;
import com.sivet.api.service.document.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Reportes descargables (§4.3). Protegidos por JWT + filtro de tenant: el Excel
 * solo incluye las ventas de la clínica del token.
 */
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private static final String XLSX_MIME =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final DocumentService documentService;

    /** GET /reportes/ventas.xlsx?rango=hoy|semana|mes  (o ?desde=YYYY-MM-DD&hasta=YYYY-MM-DD). */
    @GetMapping("/ventas.xlsx")
    public ResponseEntity<byte[]> ventasExcel(
            @RequestParam(required = false) String rango,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        DocumentResult doc = documentService.reporteVentasExcel(
                SecurityUtils.currentTenantId(), rango, desde, hasta);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(XLSX_MIME))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.filename() + "\"")
                .body(doc.content());
    }
}
