package com.inventario.ordencompra.services;

import com.inventario.ordencompra.clients.InventarioClientRest;
import com.inventario.ordencompra.clients.ProveedorClientRest;
import com.inventario.ordencompra.dto.ProveedorDto;
import com.inventario.ordencompra.models.entities.DetalleOrdenCompra;
import com.inventario.ordencompra.models.entities.OrdenCompra;
import com.inventario.ordencompra.repositories.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private ProveedorClientRest proveedorClientRest;

    @Autowired
    private InventarioClientRest inventarioClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> findAll() {
        return ordenCompraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompra> findById(Long id) {
        return ordenCompraRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompra> findByNumeroOrden(String numeroOrden) {
        return ordenCompraRepository.findByNumeroOrden(numeroOrden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> findByProveedorId(Long proveedorId) {
        return ordenCompraRepository.findByProveedorId(proveedorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> findByEstado(String estado) {
        return ordenCompraRepository.findByEstado(estado);
    }

    @Override
    @Transactional
    public OrdenCompra save(OrdenCompra ordenCompra) {
        // Calcular totales
        double total = 0.0;
        for (DetalleOrdenCompra detalle : ordenCompra.getDetalles()) {
            detalle.setOrdenCompra(ordenCompra);
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            total += subtotal;
        }
        ordenCompra.setTotal(total);
        
        return ordenCompraRepository.save(ordenCompra);
    }

    @Override
    @Transactional
    public OrdenCompra update(Long id, OrdenCompra ordenCompra) {
        return ordenCompraRepository.findById(id)
                .map(existingOrden -> {
                    existingOrden.setNumeroOrden(ordenCompra.getNumeroOrden());
                    existingOrden.setProveedorId(ordenCompra.getProveedorId());
                    existingOrden.setBodegaId(ordenCompra.getBodegaId());
                    existingOrden.setFechaEntregaEsperada(ordenCompra.getFechaEntregaEsperada());
                    existingOrden.setEstado(ordenCompra.getEstado());
                    existingOrden.setObservaciones(ordenCompra.getObservaciones());
                    
                    // Actualizar detalles
                    existingOrden.getDetalles().clear();
                    double total = 0.0;
                    for (DetalleOrdenCompra detalle : ordenCompra.getDetalles()) {
                        detalle.setOrdenCompra(existingOrden);
                        double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                        detalle.setSubtotal(subtotal);
                        total += subtotal;
                        existingOrden.getDetalles().add(detalle);
                    }
                    existingOrden.setTotal(total);
                    
                    return ordenCompraRepository.save(existingOrden);
                })
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ordenCompraRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumeroOrden(String numeroOrden) {
        return ordenCompraRepository.existsByNumeroOrden(numeroOrden);
    }

    @Override
    @Transactional
    public OrdenCompra aprobarOrden(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        if (!"PENDIENTE".equals(orden.getEstado())) {
            throw new RuntimeException("Solo se pueden aprobar órdenes en estado PENDIENTE");
        }
        
        // Verificar capacidad de la bodega antes de aprobar
        try {
            var capacidad = inventarioClientRest.getCapacidadBodega(orden.getBodegaId());
            
            // Calcular cantidad total de productos en la orden
            int cantidadTotalOrden = orden.getDetalles().stream()
                    .mapToInt(DetalleOrdenCompra::getCantidad)
                    .sum();
            
            // Calcular el porcentaje después de recibir la orden
            int cantidadActual = capacidad.getCantidadProductos() != null ? capacidad.getCantidadProductos() : 0;
            int capacidadMaxima = capacidad.getCapacidadMaxima() != null ? capacidad.getCapacidadMaxima() : 0;
            
            if (capacidadMaxima > 0) {
                int cantidadProyectada = cantidadActual + cantidadTotalOrden;
                double porcentajeProyectado = (cantidadProyectada * 100.0) / capacidadMaxima;
                
                if (porcentajeProyectado > 100) {
                    String unidad = capacidad.getUnidadCapacidad() != null ? capacidad.getUnidadCapacidad() : "unidades";
                    throw new RuntimeException(
                        String.format("La bodega '%s' no tiene capacidad suficiente. " +
                                     "Capacidad actual: %d/%d %s (%.1f%%). " +
                                     "Con esta orden llegaría a: %d %s (%.1f%%). " +
                                     "Por favor, seleccione otra bodega o edite la orden.",
                                     capacidad.getBodegaNombre(),
                                     cantidadActual, capacidadMaxima, unidad, 
                                     (cantidadActual * 100.0) / capacidadMaxima,
                                     cantidadProyectada, unidad, porcentajeProyectado)
                    );
                }
            }
        } catch (RuntimeException e) {
            throw e; // Re-throw para que llegue al controller
        } catch (Exception e) {
            // Si falla la consulta de capacidad, permitir continuar
            System.err.println("Error al verificar capacidad de bodega: " + e.getMessage());
        }
        
        orden.setEstado("APROBADA");
        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra recibirOrden(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        if (!"APROBADA".equals(orden.getEstado())) {
            throw new RuntimeException("Solo se pueden recibir órdenes en estado APROBADA");
        }
        
        // Actualizar inventario para cada detalle
        for (DetalleOrdenCompra detalle : orden.getDetalles()) {
            try {
                inventarioClientRest.agregarStock(
                    detalle.getProductoId(),
                    orden.getBodegaId(),
                    detalle.getCantidad()
                );
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar inventario: " + e.getMessage());
            }
        }
        
        orden.setEstado("RECIBIDA");
        orden.setFechaRecepcion(LocalDateTime.now());
        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra cancelarOrden(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        if ("RECIBIDA".equals(orden.getEstado())) {
            throw new RuntimeException("No se puede cancelar una orden que ya fue recibida");
        }
        
        orden.setEstado("CANCELADA");
        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> findAllOrdenesOrdenadas() {
        return ordenCompraRepository.findAllByOrderByFechaOrdenDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> calcularGastosPorProveedor() {
        List<OrdenCompra> ordenesRecibidas = ordenCompraRepository.findByEstado("RECIBIDA");
        
        Map<Long, Double> gastosPorProveedor = new java.util.HashMap<>();
        Map<Long, String> nombresProveedores = new java.util.HashMap<>();
        Map<Long, Integer> cantidadOrdenes = new java.util.HashMap<>();
        
        for (OrdenCompra orden : ordenesRecibidas) {
            Long proveedorId = orden.getProveedorId();
            Double totalOrden = orden.getTotal();
            
            gastosPorProveedor.put(proveedorId, 
                gastosPorProveedor.getOrDefault(proveedorId, 0.0) + totalOrden);
            
            cantidadOrdenes.put(proveedorId, 
                cantidadOrdenes.getOrDefault(proveedorId, 0) + 1);
            
            // Obtener nombre del proveedor
            if (!nombresProveedores.containsKey(proveedorId)) {
                try {
                    Optional<ProveedorDto> proveedorOpt = proveedorClientRest.findById(proveedorId);
                    proveedorOpt.ifPresent(p -> nombresProveedores.put(proveedorId, p.getRazonSocial()));
                } catch (Exception e) {
                    nombresProveedores.put(proveedorId, "Proveedor #" + proveedorId);
                }
            }
        }
        
        List<Map<String, Object>> resultado = new java.util.ArrayList<>();
        for (Map.Entry<Long, Double> entry : gastosPorProveedor.entrySet()) {
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("proveedorId", entry.getKey());
            item.put("proveedorNombre", nombresProveedores.getOrDefault(entry.getKey(), "Desconocido"));
            item.put("totalGastado", entry.getValue());
            item.put("cantidadOrdenes", cantidadOrdenes.get(entry.getKey()));
            resultado.add(item);
        }
        
        // Ordenar por total gastado descendente
        resultado.sort((a, b) -> Double.compare((Double)b.get("totalGastado"), (Double)a.get("totalGastado")));
        
        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        List<OrdenCompra> todasLasOrdenes = ordenCompraRepository.findAll();
        
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalOrdenes", todasLasOrdenes.size());
        stats.put("ordenesPendientes", ordenCompraRepository.findByEstado("PENDIENTE").size());
        stats.put("ordenesAprobadas", ordenCompraRepository.findByEstado("APROBADA").size());
        stats.put("ordenesRecibidas", ordenCompraRepository.findByEstado("RECIBIDA").size());
        stats.put("ordenesCanceladas", ordenCompraRepository.findByEstado("CANCELADA").size());
        
        double totalGastado = todasLasOrdenes.stream()
            .filter(o -> "RECIBIDA".equals(o.getEstado()))
            .mapToDouble(OrdenCompra::getTotal)
            .sum();
        
        stats.put("totalGastado", totalGastado);
        
        return stats;
    }
}
