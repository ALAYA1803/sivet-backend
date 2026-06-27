package com.sivet.api.repository;

import com.sivet.api.domain.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de recetas, filtrado por tenant.
 */
@Repository
public interface RecetaRepository extends JpaRepository<Receta, UUID> {

    List<Receta> findByClinica_Id(UUID clinicaId);

    Optional<Receta> findByIdAndClinica_Id(UUID id, UUID clinicaId);

    Optional<Receta> findByClinica_IdAndAtencion_Id(UUID clinicaId, UUID atencionId);
}
