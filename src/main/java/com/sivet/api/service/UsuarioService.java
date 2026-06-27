package com.sivet.api.service;

import com.sivet.api.dto.request.CambiarPasswordInicialRequest;
import com.sivet.api.dto.request.EmpleadoRequest;
import com.sivet.api.dto.request.UsuarioRequest;
import com.sivet.api.dto.response.EmpleadoCreadoResponse;
import com.sivet.api.dto.response.UsuarioResponse;

import java.util.List;
import java.util.UUID;

/**
 * Gestión de usuarios. La creación estampa el tenant ({@code clinicaId}) y guarda
 * la contraseña hasheada (§3.1). La autenticación/JWT se implementará en la capa
 * de seguridad.
 */
public interface UsuarioService {

    UsuarioResponse crear(UUID clinicaId, UsuarioRequest request);

    UsuarioResponse obtener(UUID clinicaId, UUID id);

    /**
     * Cambio de la contraseña inicial del propio usuario autenticado: hashea la nueva
     * clave y baja el flag {@code requiereCambioPassword}.
     */
    UsuarioResponse cambiarPasswordInicial(UUID usuarioId, CambiarPasswordInicialRequest request);

    /**
     * Alta de personal (ADMIN_CLINICA): crea un usuario en la clínica indicada con una
     * clave temporal generada por el backend y {@code requiereCambioPassword = true}.
     * Devuelve la clave en claro (única vez).
     */
    EmpleadoCreadoResponse crearEmpleado(UUID clinicaId, EmpleadoRequest request);

    /** Personal de la clínica (tenant) indicada. */
    List<UsuarioResponse> listarEmpleados(UUID clinicaId);
}
