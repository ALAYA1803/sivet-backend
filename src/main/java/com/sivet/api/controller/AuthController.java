package com.sivet.api.controller;

import com.sivet.api.dto.request.LoginRequest;
import com.sivet.api.dto.response.LoginResponse;
import com.sivet.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Autenticación (público). Sustituye el inseguro GET /usuarios?username=&password=
 * por un POST con body { credencial, password } que devuelve { token, payload } (§3.1).
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
