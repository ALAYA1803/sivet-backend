package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Usuario;
import com.sivet.api.dto.request.ClinicaOnboardingRequest;
import com.sivet.api.dto.response.ClinicaOnboardingResponse;
import com.sivet.api.dto.response.ClinicaResumenResponse;
import com.sivet.api.exception.ConflictException;
import com.sivet.api.mapper.ClinicaMapper;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.UsuarioRepository;
import com.sivet.api.security.Roles;
import com.sivet.api.security.TemporaryPasswordGenerator;
import com.sivet.api.service.SuperadminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperadminServiceImpl implements SuperadminService {

    private final ClinicaRepository clinicaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporaryPasswordGenerator passwordGenerator;
    private final ClinicaMapper clinicaMapper;

    @Override
    @Transactional
    public ClinicaOnboardingResponse onboardingClinica(ClinicaOnboardingRequest request) {
        // Validamos el username ANTES de crear la clínica: si chocara, la transacción
        // haría rollback igualmente, pero así devolvemos 409 con un mensaje claro.
        if (usuarioRepository.existsByUsername(request.doctorUsername())) {
            throw new ConflictException("El username ya está en uso: " + request.doctorUsername());
        }

        Clinica clinica = new Clinica();
        clinica.setNombre(request.nombre());
        clinica.setSede(request.sede());
        clinica.setRuc(request.ruc());
        clinica.setTelefono(request.telefono());
        clinica.setEmail(request.email());
        clinica.setDireccion(request.direccion());
        clinica = clinicaRepository.save(clinica);

        String passwordTemporal = passwordGenerator.generar();

        Usuario doctor = new Usuario();
        doctor.setUsername(request.doctorUsername());
        doctor.setPassword(passwordEncoder.encode(passwordTemporal)); // solo se persiste el hash
        doctor.setNombre(request.doctorNombre());
        doctor.setRol(Roles.ADMIN_CLINICA);
        doctor.setRequiereCambioPassword(true); // forzará el cambio en el primer login
        doctor.setClinica(clinica);
        usuarioRepository.save(doctor);

        return new ClinicaOnboardingResponse(clinica.getId(), doctor.getUsername(), passwordTemporal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClinicaResumenResponse> listarClinicas() {
        return clinicaRepository.findAll().stream()
                .map(clinicaMapper::toResumenResponse)
                .toList();
    }
}
