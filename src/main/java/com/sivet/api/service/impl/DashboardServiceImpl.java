package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Atencion;
import com.sivet.api.domain.entity.Cita;
import com.sivet.api.domain.entity.Venta;
import com.sivet.api.domain.enums.EstadoCita;
import com.sivet.api.domain.enums.EstadoVenta;
import com.sivet.api.domain.enums.MetodoPago;
import com.sivet.api.dto.response.CitaHoyResponse;
import com.sivet.api.dto.response.FlujoPacienteResponse;
import com.sivet.api.dto.response.ResumenMetodoPagoResponse;
import com.sivet.api.dto.response.VendidosHoyResponse;
import com.sivet.api.domain.entity.VentaItem;
import com.sivet.api.repository.AtencionRepository;
import com.sivet.api.repository.CitaRepository;
import com.sivet.api.repository.VentaRepository;
import com.sivet.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Read-models del dashboard calculados on-the-fly por tenant (§3.5).
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static final int DIAS_FLUJO = 7;
    private static final Locale ES_PE = Locale.forLanguageTag("es-PE");

    /** Color HEX de presentación por método de pago (§3.5). */
    private static final Map<MetodoPago, String> COLOR_METODO = new EnumMap<>(Map.of(
            MetodoPago.EFECTIVO, "#4CAF50",
            MetodoPago.TARJETA, "#2196F3",
            MetodoPago.YAPE, "#6C3FB5",
            MetodoPago.PLIN, "#00BCD4"
    ));

    private final AtencionRepository atencionRepository;
    private final VentaRepository ventaRepository;
    private final CitaRepository citaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FlujoPacienteResponse> flujoPacientes(UUID clinicaId) {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = hoy.minusDays(DIAS_FLUJO - 1L);

        // Conteo de atenciones por día (solo dentro de la ventana).
        Map<LocalDate, Long> conteo = atencionRepository.findByClinica_Id(clinicaId).stream()
                .map(Atencion::getFecha)
                .filter(f -> f != null)
                .map(java.time.LocalDateTime::toLocalDate)
                .filter(d -> !d.isBefore(desde) && !d.isAfter(hoy))
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()));

        List<FlujoPacienteResponse> resultado = new ArrayList<>();
        for (int i = 0; i < DIAS_FLUJO; i++) {
            LocalDate dia = desde.plusDays(i);
            resultado.add(new FlujoPacienteResponse(
                    etiquetaDia(dia),
                    conteo.getOrDefault(dia, 0L)));
        }
        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenMetodoPagoResponse> metodosPago(UUID clinicaId) {
        List<Venta> completadas = ventaRepository
                .findByClinica_IdAndEstado(clinicaId, EstadoVenta.COMPLETADA);

        Map<MetodoPago, BigDecimal> porMetodo = new EnumMap<>(MetodoPago.class);
        BigDecimal totalGeneral = BigDecimal.ZERO;
        for (Venta v : completadas) {
            BigDecimal monto = v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO;
            porMetodo.merge(v.getMetodoPago(), monto, BigDecimal::add);
            totalGeneral = totalGeneral.add(monto);
        }

        List<ResumenMetodoPagoResponse> resultado = new ArrayList<>();
        for (Map.Entry<MetodoPago, BigDecimal> e : porMetodo.entrySet()) {
            double porcentaje = totalGeneral.signum() == 0
                    ? 0d
                    : e.getValue()
                    .divide(totalGeneral, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultado.add(new ResumenMetodoPagoResponse(
                    e.getKey(),
                    e.getValue(),
                    COLOR_METODO.getOrDefault(e.getKey(), "#9E9E9E"),
                    porcentaje));
        }
        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaHoyResponse> citasHoy(UUID clinicaId) {
        LocalDate hoy = LocalDate.now();
        return citaRepository.findByClinica_IdAndFecha(clinicaId, hoy).stream()
                .filter(c -> c.getEstado() != EstadoCita.CANCELADA)
                .sorted(Comparator.comparing(Cita::getHora))
                // Cita no almacena 'tipo' ni 'vet'; se proyecta 'tipo' desde el motivo
                // y 'vet' queda vacío hasta que la agenda capture al profesional.
                .map(c -> new CitaHoyResponse(
                        c.getHora(),
                        c.getMascota().getNombre(),
                        c.getCliente().getNombre(),
                        c.getMotivo(),
                        ""))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VendidosHoyResponse vendidosHoy(UUID clinicaId) {
        LocalDate hoy = LocalDate.now();
        // Rango estricto del día: [00:00:00, 23:59:59.999999999].
        LocalDateTime desde = hoy.atStartOfDay();
        LocalDateTime hasta = hoy.atTime(LocalTime.MAX);

        // Solo ventas completadas creadas hoy.
        List<Venta> ventasHoy = ventaRepository.findByClinica_IdAndEstadoAndFechaBetween(
                clinicaId, EstadoVenta.COMPLETADA, desde, hasta);

        // KPI = suma de las cantidades de los ítems (unidades vendidas), no el nº de ventas.
        long unidades = ventasHoy.stream()
                .flatMap(v -> v.getItems().stream())
                .mapToLong(VentaItem::getCantidad)
                .sum();

        return new VendidosHoyResponse(unidades, ventasHoy.size());
    }

    /** Etiqueta de día tipo "Mié 20" (es-PE). */
    private String etiquetaDia(LocalDate dia) {
        String diaSemana = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, ES_PE);
        diaSemana = diaSemana.replace(".", "");
        if (!diaSemana.isEmpty()) {
            diaSemana = diaSemana.substring(0, 1).toUpperCase(ES_PE) + diaSemana.substring(1);
        }
        return diaSemana + " " + dia.getDayOfMonth();
    }
}
