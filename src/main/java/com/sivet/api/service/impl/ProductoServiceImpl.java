package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Producto;
import com.sivet.api.domain.enums.CategoriaProducto;
import com.sivet.api.dto.request.ProductoRequest;
import com.sivet.api.dto.request.StockPatchRequest;
import com.sivet.api.dto.response.ProductoResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.ProductoMapper;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.ProductoRepository;
import com.sivet.api.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ClinicaRepository clinicaRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(UUID clinicaId) {
        return productoRepository.findByClinica_Id(clinicaId).stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtener(UUID clinicaId, UUID id) {
        return productoMapper.toResponse(getOrThrow(clinicaId, id));
    }

    @Override
    @Transactional
    public ProductoResponse crear(UUID clinicaId, ProductoRequest request) {
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);
        Producto producto = productoMapper.toEntity(request, clinica);
        aplicarIntegridadInventario(producto);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(UUID clinicaId, UUID id, ProductoRequest request) {
        Producto producto = getOrThrow(clinicaId, id);
        productoMapper.updateEntity(producto, request);
        aplicarIntegridadInventario(producto);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse ajustarStock(UUID clinicaId, UUID id, StockPatchRequest request) {
        Producto producto = getOrThrow(clinicaId, id);
        if (producto.getCategoria() == CategoriaProducto.SERVICIO) {
            throw new BusinessException("Un producto de categoría 'Servicio' no maneja inventario");
        }
        producto.setStock(request.stock());
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    /**
     * Regla de integridad (§1.7): un 'Servicio' no maneja inventario (stock/stockMin
     * = null); cualquier otra categoría debe traer ambos numéricos.
     */
    private void aplicarIntegridadInventario(Producto p) {
        if (p.getCategoria() == CategoriaProducto.SERVICIO) {
            p.setStock(null);
            p.setStockMin(null);
        } else if (p.getStock() == null || p.getStockMin() == null) {
            throw new BusinessException(
                    "Los productos no-servicio requieren stock y stockMin numéricos");
        }
    }

    private Producto getOrThrow(UUID clinicaId, UUID id) {
        return productoRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Producto", id));
    }
}
