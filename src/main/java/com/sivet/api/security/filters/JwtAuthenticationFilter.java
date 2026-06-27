package com.sivet.api.security.filters;

import com.sivet.api.security.AuthenticatedUser;
import com.sivet.api.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Valida el JWT del header {@code Authorization: Bearer <token>} y, si es válido,
 * coloca un {@link AuthenticatedUser} en el {@code SecurityContext}. No bloquea: si
 * no hay token o es inválido, deja el contexto vacío y la autorización responderá 401.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) {
            String token = header.substring(PREFIX.length()).trim();
            try {
                Claims claims = jwtProvider.parse(token);
                AuthenticatedUser user = new AuthenticatedUser(
                        UUID.fromString(claims.get(JwtProvider.CLAIM_ID_USUARIO, String.class)),
                        claims.get(JwtProvider.CLAIM_NOMBRE, String.class),
                        claims.get(JwtProvider.CLAIM_ROL, String.class),
                        UUID.fromString(claims.get(JwtProvider.CLAIM_VETERINARIA_ID, String.class)));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.rol()));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException ex) {
                // Token inválido/expirado: contexto vacío → la autorización responderá 401.
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
