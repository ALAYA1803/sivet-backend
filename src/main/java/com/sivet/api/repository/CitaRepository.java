package com.sivet.api.repository;

import com.sivet.api.domain.entity.Cita;
import com.sivet.api.domain.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de la agenda de citas, filtrado por tenant.
 * El método {@code existsBy...EstadoNot} soporta el invariante anti-colisión (§3.3):
 * una franja (clinica, fecha, hora) está ocupada si hay una cita con estado != cancelada.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, UUID> {

    List<Cita> findByClinica_Id(UUID clinicaId);

    List<Cita> findByClinica_IdAndFecha(UUID clinicaId, LocalDate fecha);

    Optional<Cita> findByIdAndClinica_Id(UUID id, UUID clinicaId);

    boolean existsByClinica_IdAndFechaAndHoraAndEstadoNot(
            UUID clinicaId, LocalDate fecha, String hora, EstadoCita estado);
}
