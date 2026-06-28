package com.sivet.api.repository;

import com.sivet.api.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de usuarios. El login resuelve por username.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    /** Personal de una clínica (tenant). */
    List<Usuario> findByClinica_Id(UUID clinicaId);

    /**
     * Personal de una clínica (tenant) excluyendo estrictamente un rol.
     * Se usa para listar empleados sin filtrar al dueño del SaaS (SUPERADMIN).
     */
    List<Usuario> findByClinica_IdAndRolNot(UUID clinicaId, String rol);
}
