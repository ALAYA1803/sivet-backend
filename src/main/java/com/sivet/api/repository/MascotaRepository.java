package com.sivet.api.repository;

import com.sivet.api.domain.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de mascotas (pacientes), filtrado por tenant.
 * Incluye filtro por dueño (clienteId) recomendado en §2.11.
 */
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, UUID> {

    List<Mascota> findByClinica_Id(UUID clinicaId);

    List<Mascota> findByClinica_IdAndCliente_Id(UUID clinicaId, UUID clienteId);

    Optional<Mascota> findByIdAndClinica_Id(UUID id, UUID clinicaId);
}
