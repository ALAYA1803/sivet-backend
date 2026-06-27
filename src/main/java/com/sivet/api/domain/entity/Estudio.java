package com.sivet.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Examen complementario adjunto a la historia clínica (RX, LAB, ECO…).
 * <p>Regla de oro del proyecto: NO hay archivos/imágenes. Es únicamente un
 * reporte de texto; no se persisten radiografías ni PDFs.
 */
@Entity
@Table(name = "estudios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estudio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Paciente (FK → mascotas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(nullable = false)
    private String titulo;

    /** Etiqueta corta ("RX", "LAB", "ECO"). */
    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String veterinario;

    /** Tenant (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
