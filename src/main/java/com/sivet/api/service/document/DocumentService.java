package com.sivet.api.service.document;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Generación de documentos descargables (§4). Todos los métodos resuelven las
 * entidades dentro del tenant; si el recurso pertenece a otra clínica se trata como
 * inexistente (404), preservando el aislamiento multi-tenant.
 */
public interface DocumentService {

    /** PDF tipo ticket del comprobante de una venta (§4.1). */
    DocumentResult comprobanteVenta(UUID clinicaId, UUID ventaId);

    /** PDF A4 de una receta médica (§4.2). */
    DocumentResult recetaPdf(UUID clinicaId, UUID recetaId);

    /**
     * Excel de ventas del tenant en un rango (§4.3). Si {@code rango} viene informado
     * ('hoy'|'semana'|'mes') tiene prioridad; en su defecto se usan {@code desde}/{@code hasta};
     * si nada se indica, se reporta el mes en curso.
     */
    DocumentResult reporteVentasExcel(UUID clinicaId, String rango, LocalDate desde, LocalDate hasta);
}
