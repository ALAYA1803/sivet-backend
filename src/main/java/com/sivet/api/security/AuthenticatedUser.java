package com.sivet.api.security;

import java.util.UUID;

/**
 * Principal autenticado, construido a partir de los claims del JWT. Es el objeto
 * que se guarda en el {@code SecurityContext} tras validar el token.
 */
public record AuthenticatedUser(
        UUID idUsuario,
        String nombre,
        String rol,
        UUID veterinariaId
) {
}
