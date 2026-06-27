package com.sivet.api.repository;

import com.sivet.api.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a datos de clientes, siempre filtrado por tenant (clinica_id).
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    List<Cliente> findByClinica_Id(UUID clinicaId);

    Optional<Cliente> findByIdAndClinica_Id(UUID id, UUID clinicaId);
}
