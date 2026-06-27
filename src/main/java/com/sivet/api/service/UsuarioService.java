package com.sivet.api.service;

import com.sivet.api.dto.request.UsuarioRequest;
import com.sivet.api.dto.response.UsuarioResponse;

import java.util.UUID;

/**
 * Gestión de usuarios. La creación estampa el tenant ({@code clinicaId}) y guarda
 * la contraseña hasheada (§3.1). La autenticación/JWT se implementará en la capa
 * de seguridad.
 */
public interface UsuarioService {

    UsuarioResponse crear(UUID clinicaId, UsuarioRequest request);

    UsuarioResponse obtener(UUID clinicaId, UUID id);
}
