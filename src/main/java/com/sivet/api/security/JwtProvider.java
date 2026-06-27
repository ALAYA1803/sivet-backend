package com.sivet.api.security;

import com.sivet.api.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Firma y verificación de JWT (HS256). El payload contiene EXACTAMENTE los claims
 * acordados con el frontend (§3.1): {@code id_usuario}, {@code nombre}, {@code rol},
 * {@code veterinaria_id}, más {@code iat}/{@code exp}.
 */
@Component
public class JwtProvider {

    public static final String CLAIM_ID_USUARIO = "id_usuario";
    public static final String CLAIM_NOMBRE = "nombre";
    public static final String CLAIM_ROL = "rol";
    public static final String CLAIM_VETERINARIA_ID = "veterinaria_id";

    private final SecretKey key;
    private final long expirationMs;

    public JwtProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim(CLAIM_ID_USUARIO, usuario.getId().toString())
                .claim(CLAIM_NOMBRE, usuario.getNombre())
                .claim(CLAIM_ROL, usuario.getRol())
                .claim(CLAIM_VETERINARIA_ID, usuario.getClinica().getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(key)
                .compact();
    }

    /** Verifica firma y expiración; devuelve los claims. Lanza {@code JwtException} si es inválido. */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
