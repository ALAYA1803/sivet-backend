package com.sivet.api.service;

import com.sivet.api.dto.response.CitaHoyResponse;
import com.sivet.api.dto.response.FlujoPacienteResponse;
import com.sivet.api.dto.response.ResumenMetodoPagoResponse;
import com.sivet.api.dto.response.VendidosHoyResponse;

import java.util.List;
import java.util.UUID;

/**
 * Read-models del dashboard, calculados on-the-fly por tenant (§3.5).
 */
public interface DashboardService {

    /** Conteo de atenciones por día (últimos N días). */
    List<FlujoPacienteResponse> flujoPacientes(UUID clinicaId);

    /** Recaudación de ventas completadas agrupada por método de pago. */
    List<ResumenMetodoPagoResponse> metodosPago(UUID clinicaId);

    /** Citas del día actual (no canceladas), denormalizadas. */
    List<CitaHoyResponse> citasHoy(UUID clinicaId);

    /** KPI "vendidos hoy": unidades vendidas en ventas completadas del día. */
    VendidosHoyResponse vendidosHoy(UUID clinicaId);
}
