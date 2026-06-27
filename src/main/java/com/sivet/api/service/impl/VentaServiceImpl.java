package com.sivet.api.service.impl;

import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Producto;
import com.sivet.api.domain.entity.Venta;
import com.sivet.api.domain.entity.VentaItem;
import com.sivet.api.domain.enums.EstadoVenta;
import com.sivet.api.dto.request.AnularVentaRequest;
import com.sivet.api.dto.request.VentaItemRequest;
import com.sivet.api.dto.request.VentaRequest;
import com.sivet.api.dto.response.VentaResponse;
import com.sivet.api.exception.BusinessException;
import com.sivet.api.exception.ResourceNotFoundException;
import com.sivet.api.mapper.VentaMapper;
import com.sivet.api.repository.ClienteRepository;
import com.sivet.api.repository.ClinicaRepository;
import com.sivet.api.repository.ProductoRepository;
import com.sivet.api.repository.VentaRepository;
import com.sivet.api.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final ClinicaRepository clinicaRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar(UUID clinicaId) {
        return ventaRepository.findByClinica_Id(clinicaId).stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtener(UUID clinicaId, UUID id) {
        return ventaMapper.toResponse(getOrThrow(clinicaId, id));
    }

    /**
     * Registra una venta y descuenta inventario en una sola transacción (§3.2).
     * Todo-o-nada: si algún ítem no tiene stock suficiente, no se crea la venta.
     */
    @Override
    @Transactional
    public VentaResponse registrar(UUID clinicaId, VentaRequest request) {
        Clinica clinica = clinicaRepository.getReferenceById(clinicaId);
        Cliente cliente = clienteRepository.findByIdAndClinica_Id(request.clienteId(), clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));

        Venta venta = new Venta();
        venta.setClinica(clinica);
        venta.setCliente(cliente);
        venta.setFecha(request.fecha() != null ? request.fecha() : LocalDateTime.now());
        venta.setMetodoPago(request.metodoPago());
        venta.setVendedor(request.vendedor());
        venta.setTotal(request.total());
        venta.setEstado(EstadoVenta.COMPLETADA); // al crear, siempre 'completada'

        List<VentaItem> items = new ArrayList<>();
        for (VentaItemRequest itemReq : request.items()) {
            Producto producto = productoRepository
                    .findByIdAndClinica_Id(itemReq.productoId(), clinicaId)
                    .orElseThrow(() -> ResourceNotFoundException.of("Producto", itemReq.productoId()));

            descontarStock(producto, itemReq.cantidad());

            // Snapshot: nombre/precio del momento de la venta (no se re-derivan luego).
            String nombre = itemReq.nombre() != null ? itemReq.nombre() : producto.getNombre();
            items.add(ventaMapper.toItemEntity(
                    producto.getId(), nombre, itemReq.cantidad(), itemReq.precio()));
        }
        venta.setItems(items);

        return ventaMapper.toResponse(ventaRepository.save(venta));
    }

    /**
     * Anula una venta (idempotente) y restaura inventario en una sola transacción (§3.2).
     */
    @Override
    @Transactional
    public VentaResponse anular(UUID clinicaId, UUID id, AnularVentaRequest request) {
        if (request.estado() != EstadoVenta.ANULADA) {
            throw new BusinessException("El PATCH de anulación debe indicar estado 'anulada'");
        }
        Venta venta = getOrThrow(clinicaId, id);

        // Idempotencia: si ya está anulada, no se re-restaura stock.
        if (venta.getEstado() == EstadoVenta.ANULADA) {
            return ventaMapper.toResponse(venta);
        }

        venta.setEstado(EstadoVenta.ANULADA);
        venta.setMotivoAnulacion(request.motivoAnulacion());

        for (VentaItem item : venta.getItems()) {
            productoRepository.findByIdAndClinica_Id(item.getProductoId(), clinicaId)
                    .ifPresent(producto -> restaurarStock(producto, item.getCantidad()));
            // Servicios (stock null) y productos eliminados se ignoran.
        }

        return ventaMapper.toResponse(ventaRepository.save(venta));
    }

    /**
     * Descuenta stock de un producto inventariado. Endurecimiento permitido (§3.2):
     * rechaza la venta si la cantidad supera el stock. Los servicios (stock null) se ignoran.
     */
    private void descontarStock(Producto producto, int cantidad) {
        if (producto.getStock() == null) {
            return; // servicio: sin inventario
        }
        if (cantidad > producto.getStock()) {
            throw new BusinessException(
                    "Stock insuficiente para el producto '" + producto.getNombre()
                            + "' (disponible: " + producto.getStock() + ", solicitado: " + cantidad + ")");
        }
        producto.setStock(producto.getStock() - cantidad);
    }

    /** Restaura stock al anular (sin tope). Los servicios (stock null) se ignoran. */
    private void restaurarStock(Producto producto, int cantidad) {
        if (producto.getStock() == null) {
            return;
        }
        producto.setStock(producto.getStock() + cantidad);
    }

    private Venta getOrThrow(UUID clinicaId, UUID id) {
        return ventaRepository.findByIdAndClinica_Id(id, clinicaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Venta", id));
    }
}
