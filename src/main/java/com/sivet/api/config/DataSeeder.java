package com.sivet.api.config;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Usuario;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Siembra datos de demostración para la prueba de fuego: si no existe ningún usuario,
 * crea una clínica y un usuario admin, e imprime las credenciales y el Tenant ID.
 * No hace nada si ya hay datos (idempotente entre arranques).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ClinicaRepository clinicaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        Clinica clinica = new Clinica();
        clinica.setNombre("Veterinaria Demo SIVET");
        clinica.setSede("Sede Central");
        clinica.setRuc("20123456789");
        clinica.setTelefono("016543210");
        clinica.setEmail("contacto@sivet-demo.pe");
        clinica.setDireccion("Av. Siempre Viva 123, Lima");
        clinica = clinicaRepository.save(clinica);

        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setNombre("Dra. Demo");
        admin.setRol("Admin");
        admin.setClinica(clinica);
        usuarioRepository.save(admin);

        log.info("==================== SIVET · DATOS DEMO SEMBRADOS ====================");
        log.info(" Login:      POST /auth/login  {{ \"credencial\": \"admin\", \"password\": \"admin123\" }}");
        log.info(" Tenant ID:  {}  (usar en el header X-Tenant-ID)", clinica.getId());
        log.info("=====================================================================");
    }
}
