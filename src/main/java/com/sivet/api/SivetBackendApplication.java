package com.sivet.api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

import java.util.TimeZone;

// Se excluye el UserDetailsService por defecto de Spring Boot (y su "generated password"):
// la autenticación se resuelve por JWT en nuestra propia cadena de filtros.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class SivetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SivetBackendApplication.class, args);
    }

    /**
     * Fuerza la zona horaria de la JVM a Perú (America/Lima). En Render el servidor
     * corre en UTC, por lo que sin esto a partir de las 19:00 (hora de Perú) los
     * {@code LocalDate.now()} / {@code LocalDateTime.now()} saltaban al día siguiente.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
    }

}
