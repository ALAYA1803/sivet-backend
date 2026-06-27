package com.sivet.api.controller;

import com.sivet.api.dto.request.CambiarPasswordInicialRequest;
import com.sivet.api.dto.request.EmpleadoRequest;
import com.sivet.api.dto.request.UsuarioRequest;
import com.sivet.api.dto.response.EmpleadoCreadoResponse;
import com.sivet.api.dto.response.UsuarioResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Usuarios. La creación la realiza un administrador dentro de su tenant; el
 * {@code clinica_id} se deriva del token (no se envía en el body).
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.crear(SecurityUtils.currentTenantId(), request);
    }

    /**
     * Cambio de la contraseña inicial del propio usuario autenticado (tras el onboarding
     * B2B). No requiere X-Tenant-ID: opera sobre la cuenta del token, no sobre el tenant.
     */
    @PostMapping("/cambiar-password-inicial")
    public UsuarioResponse cambiarPasswordInicial(@Valid @RequestBody CambiarPasswordInicialRequest request) {
        return usuarioService.cambiarPasswordInicial(SecurityUtils.currentUser().idUsuario(), request);
    }

    /**
     * Alta de personal por el dueño de la clínica. El {@code clinicaId} se hereda del
     * token; devuelve la contraseña temporal en claro para entregarla al empleado.
     */
    @PostMapping("/empleados")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN_CLINICA')")
    public EmpleadoCreadoResponse crearEmpleado(@Valid @RequestBody EmpleadoRequest request) {
        return usuarioService.crearEmpleado(SecurityUtils.currentTenantId(), request);
    }

    /** Listado del personal de la clínica actual (tenant del token). */
    @GetMapping("/empleados")
    @PreAuthorize("hasRole('ADMIN_CLINICA')")
    public List<UsuarioResponse> listarEmpleados() {
        return usuarioService.listarEmpleados(SecurityUtils.currentTenantId());
    }
}
