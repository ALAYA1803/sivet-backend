package com.sivet.api.domain.entity;

import com.sivet.api.domain.enums.Especie;
import com.sivet.api.domain.enums.Sexo;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Paciente de la clínica. Pertenece a un {@link Cliente} y a un tenant.
 * <p>Regla de oro del proyecto: NO se manejan archivos/imágenes — {@code foto}
 * es simplemente un String que admite {@code null}.
 */
@Entity
@Table(name = "mascotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    @Column(nullable = false)
    private String raza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexo sexo;

    /** Edad legible, p. ej. "3 años" (texto, no número). */
    @Column(nullable = false)
    private String edad;

    /** Peso en kilogramos. */
    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private String color;

    /** URL de la foto o null. Campo siempre presente (puede ser null). Sin archivos adjuntos. */
    @Column
    private String foto;

    @Column(nullable = false)
    private boolean esterilizada;

    /** Opcional. */
    @Column
    private String microchip;

    /** Dueño (FK → clientes.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /** Tenant (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
