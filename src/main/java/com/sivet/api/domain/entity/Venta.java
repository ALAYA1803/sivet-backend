package com.sivet.api.domain.entity;

import com.sivet.api.domain.enums.EstadoVenta;
import com.sivet.api.domain.enums.MetodoPago;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Comprobante del punto de venta (POS). Contiene un array embebido de
 * {@link VentaItem} persistido en la tabla hija {@code venta_items}.
 * Al crear, el estado siempre es 'completada' (lo fija el flujo de negocio).
 */
@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Fecha/hora de emisión, ISO 8601 c/hora. */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Comprador (FK → clientes.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "venta_items",
            joinColumns = @JoinColumn(name = "venta_id")
    )
    private List<VentaItem> items = new ArrayList<>();

    /** Total en soles (PEN). */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado;

    /** Nombre del usuario que vendió. */
    @Column(nullable = false)
    private String vendedor;

    /** Presente solo cuando estado == 'anulada'. */
    @Column
    private String motivoAnulacion;

    /** Tenant (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
