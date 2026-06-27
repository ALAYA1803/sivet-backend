package com.sivet.api.repository;

import com.sivet.api.domain.entity.Venta;
import com.sivet.api.domain.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de ventas (POS), filtrado por tenant.
 * Filtros recomendados (§2.11): por estado y por rango de fechas (reportes).
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, UUID> {

    List<Venta> findByClinica_Id(UUID clinicaId);

    List<Venta> findByClinica_IdAndEstado(UUID clinicaId, EstadoVenta estado);

    List<Venta> findByClinica_IdAndFechaBetween(UUID clinicaId, LocalDateTime desde, LocalDateTime hasta);

    Optional<Venta> findByIdAndClinica_Id(UUID id, UUID clinicaId);
}
