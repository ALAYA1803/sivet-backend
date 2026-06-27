package com.sivet.api.repository;

import com.sivet.api.domain.entity.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de atenciones (historia clínica), filtrado por tenant.
 * Incluye el filtro por mascota ordenado por fecha desc (§2.11).
 */
@Repository
public interface AtencionRepository extends JpaRepository<Atencion, UUID> {

    List<Atencion> findByClinica_Id(UUID clinicaId);

    List<Atencion> findByClinica_IdAndMascota_IdOrderByFechaDesc(UUID clinicaId, UUID mascotaId);

    Optional<Atencion> findByIdAndClinica_Id(UUID id, UUID clinicaId);
}
