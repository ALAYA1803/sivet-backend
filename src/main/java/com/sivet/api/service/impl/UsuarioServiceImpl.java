package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Usuario;
import com.sivet.api.dto.request.CambiarPasswordInicialRequest;
import com.sivet.api.dto.request.EmpleadoRequest;
import com.sivet.api.dto.request.UsuarioRequest;
import com.sivet.api.dto.response.EmpleadoCreadoResponse;
import com.sivet.api.dto.response.UsuarioResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ConflictException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.UsuarioMapper;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.UsuarioRepository;
import com.sivet.api.security.Roles;
import com.sivet.api.security.TemporaryPasswordGenerator;
import com.sivet.api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final UsuarioMapper usuarioMapper;

    /** Hash de contraseñas (§3.1); bean compartido definido en SecurityConfig. */
    private final PasswordEncoder passwordEncoder;

    private final TemporaryPasswordGenerator passwordGenerator;

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

    @Override
    @Transactional
    public UsuarioResponse cambiarPasswordInicial(UUID usuarioId, CambiarPasswordInicialRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", usuarioId));

        usuario.setPassword(passwordEncoder.encode(request.nuevaPassword()));
        usuario.setRequiereCambioPassword(false);

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public EmpleadoCreadoResponse crearEmpleado(UUID clinicaId, EmpleadoRequest request) {
        if (!Roles.ROLES_EMPLEADO.contains(request.rol())) {
            throw new BusinessException("Rol de empleado no permitido: " + request.rol()
                    + ". Valores válidos: " + Roles.ROLES_EMPLEADO);
        }
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new ConflictException("El username ya está en uso: " + request.username());
        }
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Clínica", clinicaId));

        String passwordTemporal = passwordGenerator.generar();

        Usuario empleado = new Usuario();
        empleado.setUsername(request.username());
        empleado.setPassword(passwordEncoder.encode(passwordTemporal)); // solo se persiste el hash
        empleado.setNombre(request.nombre());
        empleado.setRol(request.rol());
        empleado.setRequiereCambioPassword(true); // forzará el cambio en el primer login
        empleado.setClinica(clinica); // clinicaId heredado del admin autenticado
        usuarioRepository.save(empleado);

        return new EmpleadoCreadoResponse(empleado.getUsername(), passwordTemporal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarEmpleados(UUID clinicaId) {
        // Excluye al dueño del SaaS (SUPERADMIN): no es personal de la clínica.
        return usuarioRepository.findByClinica_IdAndRolNot(clinicaId, Roles.SUPERADMIN).stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }
}
