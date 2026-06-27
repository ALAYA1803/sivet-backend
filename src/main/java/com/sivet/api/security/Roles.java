package com.sivet.api.security;

import java.util.Set;

/**
 * Roles de la plataforma. El valor se persiste tal cual en {@code usuarios.rol} y el
 * filtro de JWT lo expone como autoridad {@code ROLE_<rol>}; por eso
 * {@code @PreAuthorize("hasRole('SUPERADMIN')")} casa con el rol {@code SUPERADMIN}.
 */
public final class Roles {

    /** Dueño del SaaS: gestiona el backoffice B2B (alta de clínicas y sus admins). */
    public static final String SUPERADMIN = "SUPERADMIN";

    /** Administrador (doctor principal) de una clínica/tenant. */
    public static final String ADMIN_CLINICA = "ADMIN_CLINICA";

    /** Personal clínico. */
    public static final String VETERINARIO = "Veterinario";

    /** Personal de recepción/caja. */
    public static final String RECEPCIONISTA = "Recepcionista";

    /**
     * Roles que un ADMIN_CLINICA puede asignar al dar de alta a su personal. No incluye
     * SUPERADMIN ni ADMIN_CLINICA: un admin no crea otros admins ni superadmins por esta vía.
     */
    public static final Set<String> ROLES_EMPLEADO = Set.of(VETERINARIO, RECEPCIONISTA);

    private Roles() {
    }
}
