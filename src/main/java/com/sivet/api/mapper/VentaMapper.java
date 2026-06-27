package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Venta;
import com.sivet.api.domain.entity.VentaItem;
import com.sivet.api.dto.response.VentaItemResponse;
import com.sivet.api.dto.response.VentaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapeo de {@link Venta} y sus líneas embebidas (snapshot). La construcción de la
 * entidad, el cálculo de inventario y el estado los gestiona el servicio (§3.2).
 */
@Component
public class VentaMapper {

    public VentaResponse toResponse(Venta v) {
        List<VentaItemResponse> items = v.getItems().stream()
                .map(i -> new VentaItemResponse(
                        i.getProductoId(),
                        i.getNombre(),
                        i.getCantidad(),
                        i.getPrecio()))
                .toList();
        return new VentaResponse(
                v.getId(),
                v.getFecha(),
                v.getCliente().getId(),
                items,
                v.getTotal(),
                v.getMetodoPago(),
                v.getEstado(),
                v.getVendedor(),
                v.getMotivoAnulacion()
        );
    }

    /** Construye una línea embebida (snapshot de nombre/precio aplicados). */
    public VentaItem toItemEntity(java.util.UUID productoId, String nombre,
                                  Integer cantidad, java.math.BigDecimal precio) {
        return new VentaItem(productoId, nombre, cantidad, precio);
    }
}
