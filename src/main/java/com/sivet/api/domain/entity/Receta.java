package com.sivet.api.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Receta médica vinculada 1:1 a una {@link Atencion}. Contiene un array embebido
 * de líneas ({@link RecetaItem}) persistido en la tabla hija {@code receta_items}.
 * Una receta siempre tiene ≥ 1 ítem (se valida en la capa de servicio).
 */
@Entity
@Table(name = "recetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Atención asociada (FK → atenciones.id). Relación 1:1 inversa de
     * {@link Atencion#getReceta()}; ambas columnas se mantienen consistentes
     * en la misma transacción (capa de servicio).
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_id", unique = true)
    private Atencion atencion;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "receta_items",
            joinColumns = @JoinColumn(name = "receta_id")
    )
    private List<RecetaItem> items = new ArrayList<>();

    /** Tenant (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
