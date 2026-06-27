package com.sivet.api.controller;

import com.sivet.api.dto.request.UsuarioRequest;
import com.sivet.api.dto.response.UsuarioResponse;
import com.sivet.api.security.SecurityUtils;
import com.sivet.api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
