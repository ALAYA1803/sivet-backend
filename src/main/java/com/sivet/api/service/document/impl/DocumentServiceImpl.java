package com.sivet.api.service.document.impl;

import com.sivet.api.domain.entity.Atencion;
import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.domain.entity.Receta;
import com.sivet.api.domain.entity.Venta;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.RecetaRepository;
import com.sivet.api.repository.VentaRepository;
import com.sivet.api.service.document.DocumentResult;
import com.sivet.api.service.document.DocumentService;
import com.sivet.api.service.document.ExcelDocumentGenerator;
import com.sivet.api.service.document.PdfDocumentGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final VentaRepository ventaRepository;
    private final RecetaRepository recetaRepository;
    private final ClinicaRepository clinicaRepository;
    private final PdfDocumentGenerator pdfGenerator;
    private final ExcelDocumentGenerator excelGenerator;

    @Override
    @Transactional(readOnly = true)
    public DocumentResult comprobanteVenta(UUID clinicaId, UUID ventaId) {
        Clinica clinica = clinicaOrThrow(clinicaId);
        Venta venta = ventaRepository.findByIdAndClinica_Id(ventaId, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Venta", ventaId));

        byte[] pdf = pdfGenerator.comprobanteVenta(clinica, venta);
        return new DocumentResult(pdf, "ticket-" + ventaId + ".pdf");
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResult recetaPdf(UUID clinicaId, UUID recetaId) {
        Clinica clinica = clinicaOrThrow(clinicaId);
        Receta receta = recetaRepository.findByIdAndClinica_Id(recetaId, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Receta", recetaId));

        // La receta apunta a su atención; de ahí salen mascota, dueño, veterinario y fecha.
        Atencion atencion = receta.getAtencion();
        Mascota mascota = atencion != null ? atencion.getMascota() : null;
        Cliente cliente = mascota != null ? mascota.getCliente() : null;

        byte[] pdf = pdfGenerator.recetaMedica(clinica, cliente, mascota, atencion, receta);
        return new DocumentResult(pdf, "receta-" + recetaId + ".pdf");
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResult reporteVentasExcel(UUID clinicaId, String rango, LocalDate desde, LocalDate hasta) {
        Clinica clinica = clinicaOrThrow(clinicaId);

        LocalDate[] ventana = resolverRango(rango, desde, hasta);
        LocalDate inicio = ventana[0];
        LocalDate fin = ventana[1];

        LocalDateTime desdeDt = inicio.atStartOfDay();
        LocalDateTime hastaDt = fin.atTime(LocalTime.MAX);

        List<Venta> ventas = ventaRepository.findByClinica_IdAndFechaBetween(clinicaId, desdeDt, hastaDt);

        byte[] xlsx = excelGenerator.reporteVentas(clinica, ventas, inicio, fin);
        String sufijo = (rango != null && !rango.isBlank()) ? rango : (inicio + "_" + fin);
        return new DocumentResult(xlsx, "reporte-ventas-" + sufijo + ".xlsx");
    }

    /** Determina [inicio, fin] según rango con prioridad, luego desde/hasta, luego mes actual. */
    private LocalDate[] resolverRango(String rango, LocalDate desde, LocalDate hasta) {
        LocalDate hoy = LocalDate.now();
        if (rango != null && !rango.isBlank()) {
            return switch (rango.toLowerCase()) {
                case "hoy" -> new LocalDate[]{hoy, hoy};
                case "semana" -> new LocalDate[]{hoy.minusDays(6), hoy};
                case "mes" -> new LocalDate[]{hoy.withDayOfMonth(1), hoy};
                default -> new LocalDate[]{hoy.withDayOfMonth(1), hoy};
            };
        }
        if (desde != null && hasta != null) {
            return new LocalDate[]{desde, hasta};
        }
        return new LocalDate[]{hoy.withDayOfMonth(1), hoy}; // por defecto: mes en curso
    }

    private Clinica clinicaOrThrow(UUID clinicaId) {
        return clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Clínica", clinicaId));
    }
}
