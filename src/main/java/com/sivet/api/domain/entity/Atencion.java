package com.sivet.api.domain.entity;

import com.sivet.api.domain.enums.TipoAtencion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento inmutable de la historia clínica (no se edita ni borra una vez creado).
 * Puede emitir a lo sumo una {@link Receta} (relación 0..1).
 */
@Entity
@Table(name = "atenciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Paciente (FK → mascotas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    /** Fecha/hora ISO 8601, p. ej. "2026-05-24T10:30:00". */
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtencion tipo;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private String diagnostico;

    @Column(nullable = false)
    private String tratamiento;

    @Column(nullable = false)
    private String veterinario;

    @Column(nullable = false)
    private Double temperatura;

    @Column(nullable = false)
    private Integer frecCardiaca;

    @Column(nullable = false)
    private Integer frecRespiratoria;

    /**
     * Receta emitida (FK → recetas.id). Opcional (0..1). Relación 1:1 con
     * {@link Receta#getAtencion()}; ambas columnas se mantienen consistentes.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", unique = true)
    private Receta receta;

    /** Tenant (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
