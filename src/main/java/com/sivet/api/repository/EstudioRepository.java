package com.sivet.api.repository;

import com.sivet.api.domain.entity.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de estudios (reportes de texto), filtrado por tenant.
 * Incluye el filtro por mascota recomendado en §2.11.
 */
@Repository
public interface EstudioRepository extends JpaRepository<Estudio, UUID> {

    List<Estudio> findByClinica_Id(UUID clinicaId);

    List<Estudio> findByClinica_IdAndMascota_Id(UUID clinicaId, UUID mascotaId);

    Optional<Estudio> findByIdAndClinica_Id(UUID id, UUID clinicaId);
}
