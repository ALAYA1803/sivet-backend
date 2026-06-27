package com.sivet.api.mapper;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Producto;
import com.sivet.api.dto.request.ProductoRequest;
import com.sivet.api.dto.response.ProductoResponse;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre {@link Producto} y sus DTOs. La regla de integridad de inventario
 * (Servicio ⇒ stock/stockMin null) la aplica el servicio antes de mapear.
 */
@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest req, Clinica clinica) {
        Producto p = new Producto();
        p.setCodigo(req.codigo());
        p.setNombre(req.nombre());
        p.setCategoria(req.categoria());
        p.setPrecio(req.precio());
        p.setStock(req.stock());
        p.setStockMin(req.stockMin());
        p.setUnidad(req.unidad());
        p.setClinica(clinica);
        return p;
    }

    public void updateEntity(Producto p, ProductoRequest req) {
        p.setCodigo(req.codigo());
        p.setNombre(req.nombre());
        p.setCategoria(req.categoria());
        p.setPrecio(req.precio());
        p.setStock(req.stock());
        p.setStockMin(req.stockMin());
        p.setUnidad(req.unidad());
    }

    public ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                p.getPrecio(),
                p.getStock(),
                p.getStockMin(),
                p.getUnidad()
        );
    }
}
