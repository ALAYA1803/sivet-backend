package com.sivet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

// Se excluye el UserDetailsService por defecto de Spring Boot (y su "generated password"):
// la autenticación se resuelve por JWT en nuestra propia cadena de filtros.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class SivetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SivetBackendApplication.class, args);
    }

}
