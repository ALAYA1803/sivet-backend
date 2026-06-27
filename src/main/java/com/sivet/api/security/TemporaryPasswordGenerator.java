package com.sivet.api.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Genera contraseñas temporales aleatorias para el onboarding B2B. No usamos correo:
 * la clave en claro se devuelve UNA vez al frontend y solo se persiste su hash.
 */
@Component
public class TemporaryPasswordGenerator {

    /** Sin caracteres ambiguos (0/O, 1/l/I) para que sea fácil de dictar/teclear. */
    private static final char[] ALFABETO =
            "ABCDEFGHJKMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789".toCharArray();

    private static final int LONGITUD = 8;

    private final SecureRandom random = new SecureRandom();

    public String generar() {
        StringBuilder sb = new StringBuilder(LONGITUD);
        for (int i = 0; i < LONGITUD; i++) {
            sb.append(ALFABETO[random.nextInt(ALFABETO.length)]);
        }
        return sb.toString();
    }
}
