package com.sivet.api.domain.entity;

import com.sivet.api.domain.enums.CategoriaProducto;
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

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Ítem vendible del catálogo (producto físico o servicio).
 * <p>Regla de integridad: si {@code categoria == SERVICIO} ⇒ {@code stock} y
 * {@code stockMin} son {@code null} (sin inventario). En otras categorías, ambos
 * son numéricos. Stock crítico ⇔ stock != null && stockMin != null && stock <= stockMin.
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** SKU/código interno. */
    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProducto categoria;

    /** Precio unitario en soles (PEN). */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    /** Existencias. null para categoría 'Servicio'. */
    @Column
    private Integer stock;

    /** Umbral de alerta. null para servicios. */
    @Column
    private Integer stockMin;

    /** p. ej. "unidad", "caja", "ml". */
    @Column(nullable = false)
    private String unidad;

    /** Tenant (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
