package com.sivet.api.service;

import com.sivet.api.dto.request.LoginRequest;
import com.sivet.api.dto.response.LoginResponse;

/**
 * Autenticación de usuarios (§3.1).
 */
public interface AuthService {

    /**
     * Verifica credenciales contra el hash almacenado y devuelve un JWT firmado con
     * los claims acordados. Lanza credenciales inválidas (401) con mensaje genérico
     * si no hay match (sin revelar si el usuario existe).
     */
    LoginResponse login(LoginRequest request);
}
