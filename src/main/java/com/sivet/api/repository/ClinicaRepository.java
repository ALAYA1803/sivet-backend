package com.sivet.api.repository;

import com.sivet.api.domain.entity.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Acceso a datos de clínicas (tenants). Es la entidad raíz: no se filtra por tenant.
 */
@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, UUID> {
}
