package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Usuario;
import com.sivet.api.dto.request.UsuarioRequest;
import com.sivet.api.dto.response.UsuarioResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.UsuarioMapper;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.UsuarioRepository;
import com.sivet.api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final UsuarioMapper usuarioMapper;

    /** Hash de contraseñas (§3.1); bean compartido definido en SecurityConfig. */
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse crear(UUID clinicaId, UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BusinessException("El username ya está en uso: " + request.username());
        }
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Clínica", clinicaId));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password())); // nunca en texto plano
        usuario.setNombre(request.nombre());
        usuario.setRol(request.rol());
        usuario.setClinica(clinica);

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtener(UUID clinicaId, UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .filter(u -> u.getClinica().getId().equals(clinicaId))
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", id));
        return usuarioMapper.toResponse(usuario);
    }
}
