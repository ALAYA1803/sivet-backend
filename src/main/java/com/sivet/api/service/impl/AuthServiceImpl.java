package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Usuario;
import com.sivet.api.dto.request.LoginRequest;
import com.sivet.api.dto.response.JwtPayload;
import com.sivet.api.dto.response.LoginResponse;
import com.sivet.api.exception.InvalidCredentialsException;
import com.sivet.api.repository.UsuarioRepository;
import com.sivet.api.security.JwtProvider;
import com.sivet.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String MENSAJE_GENERICO = "Usuario o contraseña incorrectos.";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.credencial())
                .orElseThrow(() -> new InvalidCredentialsException(MENSAJE_GENERICO));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new InvalidCredentialsException(MENSAJE_GENERICO);
        }

        String token = jwtProvider.generateToken(usuario);
        JwtPayload payload = new JwtPayload(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getRol(),
                usuario.getClinica().getId());

        return new LoginResponse(token, payload);
    }
}
