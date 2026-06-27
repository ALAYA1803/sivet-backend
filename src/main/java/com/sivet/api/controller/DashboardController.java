package com.sivet.api.controller;

import com.sivet.api.dto.response.CitaHoyResponse;
import com.sivet.api.dto.response.FlujoPacienteResponse;
import com.sivet.api.dto.response.ResumenMetodoPagoResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-models del dashboard (§2.10/§3.5). Solo lectura, calculados por tenant.
 * Las rutas conservan los nombres exactos del contrato (sin prefijo de recurso).
 */
@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/flujoPacientes")
    public List<FlujoPacienteResponse> flujoPacientes() {
        return dashboardService.flujoPacientes(SecurityUtils.currentTenantId());
    }

    @GetMapping("/metodosPago")
    public List<ResumenMetodoPagoResponse> metodosPago() {
        return dashboardService.metodosPago(SecurityUtils.currentTenantId());
    }

    @GetMapping("/citasHoy")
    public List<CitaHoyResponse> citasHoy() {
        return dashboardService.citasHoy(SecurityUtils.currentTenantId());
    }
}
