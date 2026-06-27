package com.sivet.api.repository;

import com.sivet.api.domain.entity.Producto;
import com.sivet.api.domain.enums.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos del catálogo/inventario, filtrado por tenant.
 * Filtros recomendados (§2.11): por categoría y stock crítico.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    List<Producto> findByClinica_Id(UUID clinicaId);

    List<Producto> findByClinica_IdAndCategoria(UUID clinicaId, CategoriaProducto categoria);

    Optional<Producto> findByIdAndClinica_Id(UUID id, UUID clinicaId);
}
