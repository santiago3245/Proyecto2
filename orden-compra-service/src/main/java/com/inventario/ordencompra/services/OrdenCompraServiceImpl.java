package com.inventario.ordencompra.services;

import com.inventario.ordencompra.dtos.*;
import com.inventario.ordencompra.entities.DetalleOrdenCompra;
import com.inventario.ordencompra.entities.EstadoOrden;
import com.inventario.ordencompra.entities.OrdenCompra;
import com.inventario.ordencompra.feign.InventarioClientRest;
import com.inventario.ordencompra.feign.ProductoClientRest;
import com.inventario.ordencompra.feign.ProveedorClientRest;
import com.inventario.ordencompra.repositories.DetalleOrdenCompraRepository;
import com.inventario.ordencompra.repositories.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

    @Autowired
    private ProductoClientRest productoClient;

    @Autowired
    private ProveedorClientRest proveedorClient;

    @Autowired
    private InventarioClientRest inventarioClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDto> findAll() {
        List<OrdenCompra> ordenes = ordenCompraRepository.findAll();
        return ordenes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompraDto findById(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con ID: " + id));
        return convertToDto(orden);
    }

    @Override
    @Transactional
    public OrdenCompraDto create(CrearOrdenRequest request) {
        // Validar que exista el proveedor
        try {
            ProveedorDto proveedor = proveedorClient.getProveedorById(request.getProveedorId());
            if (proveedor == null) {
                throw new RuntimeException("Proveedor no encontrado con ID: " + request.getProveedorId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el proveedor: " + e.getMessage());
        }

        // Crear la orden
        OrdenCompra orden = new OrdenCompra();
        orden.setNumeroOrden(generarNumeroOrden());
        orden.setProveedorId(request.getProveedorId());
        orden.setBodegaId(request.getBodegaId());
        orden.setObservaciones(request.getObservaciones());
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setFechaCreacion(LocalDateTime.now());

        // Crear los detalles
        List<DetalleOrdenCompra> detalles = new ArrayList<>();
        for (CrearOrdenRequest.DetalleRequest detalleReq : request.getDetalles()) {
            try {
                ProductoDto producto = productoClient.getProductoById(detalleReq.getProductoId());
                if (producto == null) {
                    throw new RuntimeException("Producto no encontrado con ID: " + detalleReq.getProductoId());
                }

                DetalleOrdenCompra detalle = new DetalleOrdenCompra();
                detalle.setProductoId(detalleReq.getProductoId());
                detalle.setCantidad(detalleReq.getCantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.calcularSubtotal();

                detalles.add(detalle);
            } catch (Exception e) {
                throw new RuntimeException("Error al validar el producto " + detalleReq.getProductoId() + ": " + e.getMessage());
            }
        }

        // Guardar la orden primero
        orden = ordenCompraRepository.save(orden);

        // Asignar el ID de la orden a los detalles y guardarlos
        for (DetalleOrdenCompra detalle : detalles) {
            detalle.setOrdenCompraId(orden.getId());
            orden.addDetalle(detalle);
        }

        orden.calcularTotal();
        orden = ordenCompraRepository.save(orden);

        return convertToDto(orden);
    }

    @Override
    @Transactional
    public OrdenCompraDto update(Long id, CrearOrdenRequest request) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con ID: " + id));

        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new RuntimeException("Solo se pueden modificar órdenes en estado PENDIENTE");
        }

        // Validar proveedor
        try {
            ProveedorDto proveedor = proveedorClient.getProveedorById(request.getProveedorId());
            if (proveedor == null) {
                throw new RuntimeException("Proveedor no encontrado con ID: " + request.getProveedorId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el proveedor: " + e.getMessage());
        }

        // Actualizar datos básicos
        orden.setProveedorId(request.getProveedorId());
        orden.setBodegaId(request.getBodegaId());
        orden.setObservaciones(request.getObservaciones());

        // Eliminar detalles anteriores
        orden.getDetalles().clear();
        detalleOrdenCompraRepository.deleteAll(detalleOrdenCompraRepository.findByOrdenCompraId(id));

        // Crear nuevos detalles
        for (CrearOrdenRequest.DetalleRequest detalleReq : request.getDetalles()) {
            try {
                ProductoDto producto = productoClient.getProductoById(detalleReq.getProductoId());
                if (producto == null) {
                    throw new RuntimeException("Producto no encontrado con ID: " + detalleReq.getProductoId());
                }

                DetalleOrdenCompra detalle = new DetalleOrdenCompra();
                detalle.setOrdenCompraId(orden.getId());
                detalle.setProductoId(detalleReq.getProductoId());
                detalle.setCantidad(detalleReq.getCantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.calcularSubtotal();

                orden.addDetalle(detalle);
            } catch (Exception e) {
                throw new RuntimeException("Error al validar el producto " + detalleReq.getProductoId() + ": " + e.getMessage());
            }
        }

        orden.calcularTotal();
        orden = ordenCompraRepository.save(orden);

        return convertToDto(orden);
    }

    @Override
    @Transactional
    public OrdenCompraDto aprobarOrden(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con ID: " + id));

        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new RuntimeException("Solo se pueden aprobar órdenes en estado PENDIENTE");
        }

        // Consultar capacidad actual de la bodega
        BodegaCapacidadDto capacidadBodega;
        try {
            capacidadBodega = inventarioClient.getBodegaCapacidad(orden.getBodegaId());
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la capacidad de la bodega: " + e.getMessage());
        }

        // Calcular cantidad total de la orden
        int cantidadTotalOrden = orden.getDetalles().stream()
                .mapToInt(DetalleOrdenCompra::getCantidad)
                .sum();

        // Calcular porcentaje proyectado
        int capacidadActual = capacidadBodega.getCantidadProductos();
        int capacidadMaxima = capacidadBodega.getCapacidadMaxima();
        double porcentajeProyectado = ((double) (capacidadActual + cantidadTotalOrden) / capacidadMaxima) * 100;

        // Validar si excede la capacidad
        if (porcentajeProyectado > 100) {
            throw new RuntimeException(String.format(
                    "No se puede aprobar la orden. La bodega '%s' excedería su capacidad. " +
                    "Capacidad máxima: %d, Ocupación actual: %d (%.2f%%), " +
                    "Productos de la orden: %d, Ocupación proyectada: %d (%.2f%%)",
                    capacidadBodega.getBodegaNombre(),
                    capacidadMaxima,
                    capacidadActual,
                    capacidadBodega.getPorcentajeUtilizacion(),
                    cantidadTotalOrden,
                    capacidadActual + cantidadTotalOrden,
                    porcentajeProyectado
            ));
        }

        // Aprobar la orden
        orden.setEstado(EstadoOrden.APROBADA);
        orden.setFechaAprobacion(LocalDateTime.now());
        orden = ordenCompraRepository.save(orden);

        return convertToDto(orden);
    }

    @Override
    @Transactional
    public OrdenCompraDto recibirOrden(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con ID: " + id));

        if (orden.getEstado() != EstadoOrden.APROBADA) {
            throw new RuntimeException("Solo se pueden recibir órdenes en estado APROBADA");
        }

        // Agregar stock para cada detalle
        for (DetalleOrdenCompra detalle : orden.getDetalles()) {
            try {
                AgregarStockRequest stockRequest = new AgregarStockRequest(
                        orden.getBodegaId(),
                        detalle.getProductoId(),
                        detalle.getCantidad(),
                        "Recepción de orden de compra: " + orden.getNumeroOrden()
                );
                inventarioClient.agregarStock(stockRequest);
            } catch (Exception e) {
                throw new RuntimeException("Error al agregar stock del producto " + detalle.getProductoId() + ": " + e.getMessage());
            }
        }

        // Marcar como recibida
        orden.setEstado(EstadoOrden.RECIBIDA);
        orden.setFechaRecepcion(LocalDateTime.now());
        orden = ordenCompraRepository.save(orden);

        return convertToDto(orden);
    }

    @Override
    @Transactional
    public OrdenCompraDto cancelarOrden(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada con ID: " + id));

        if (orden.getEstado() == EstadoOrden.RECIBIDA) {
            throw new RuntimeException("No se puede cancelar una orden ya recibida");
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        orden = ordenCompraRepository.save(orden);

        return convertToDto(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDto> findByEstado(EstadoOrden estado) {
        List<OrdenCompra> ordenes = ordenCompraRepository.findByEstado(estado);
        return ordenes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDto> findByProveedorId(Long proveedorId) {
        List<OrdenCompra> ordenes = ordenCompraRepository.findByProveedorId(proveedorId);
        return ordenes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDto> obtenerHistorial() {
        List<OrdenCompra> ordenes = ordenCompraRepository.findAll();
        return ordenes.stream()
                .sorted(Comparator.comparing(OrdenCompra::getFechaCreacion).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastoPorProveedorDto> obtenerGastosPorProveedor() {
        List<OrdenCompra> ordenes = ordenCompraRepository.findAll();
        
        Map<Long, List<OrdenCompra>> ordenesPorProveedor = ordenes.stream()
                .filter(o -> o.getEstado() == EstadoOrden.RECIBIDA)
                .collect(Collectors.groupingBy(OrdenCompra::getProveedorId));

        List<GastoPorProveedorDto> gastos = new ArrayList<>();
        
        for (Map.Entry<Long, List<OrdenCompra>> entry : ordenesPorProveedor.entrySet()) {
            Long proveedorId = entry.getKey();
            List<OrdenCompra> ordenesProveedor = entry.getValue();
            
            String proveedorNombre = "Proveedor " + proveedorId;
            try {
                ProveedorDto proveedor = proveedorClient.getProveedorById(proveedorId);
                if (proveedor != null) {
                    proveedorNombre = proveedor.getNombre();
                }
            } catch (Exception e) {
                // Si falla, usar el nombre por defecto
            }

            BigDecimal totalGastado = ordenesProveedor.stream()
                    .map(OrdenCompra::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            GastoPorProveedorDto gasto = new GastoPorProveedorDto(
                    proveedorId,
                    proveedorNombre,
                    (long) ordenesProveedor.size(),
                    totalGastado
            );
            gastos.add(gasto);
        }

        return gastos.stream()
                .sorted(Comparator.comparing(GastoPorProveedorDto::getTotalGastado).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadisticasDto obtenerEstadisticas() {
        List<OrdenCompra> todasOrdenes = ordenCompraRepository.findAll();
        
        EstadisticasDto stats = new EstadisticasDto();
        stats.setTotalOrdenes((long) todasOrdenes.size());
        
        stats.setOrdenesPendientes(todasOrdenes.stream()
                .filter(o -> o.getEstado() == EstadoOrden.PENDIENTE)
                .count());
        
        stats.setOrdenesAprobadas(todasOrdenes.stream()
                .filter(o -> o.getEstado() == EstadoOrden.APROBADA)
                .count());
        
        stats.setOrdenesRecibidas(todasOrdenes.stream()
                .filter(o -> o.getEstado() == EstadoOrden.RECIBIDA)
                .count());
        
        stats.setOrdenesCanceladas(todasOrdenes.stream()
                .filter(o -> o.getEstado() == EstadoOrden.CANCELADA)
                .count());
        
        BigDecimal totalGastado = todasOrdenes.stream()
                .filter(o -> o.getEstado() == EstadoOrden.RECIBIDA)
                .map(OrdenCompra::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.setTotalGastado(totalGastado);
        
        if (stats.getOrdenesRecibidas() > 0) {
            BigDecimal promedio = totalGastado.divide(
                    BigDecimal.valueOf(stats.getOrdenesRecibidas()),
                    2,
                    RoundingMode.HALF_UP
            );
            stats.setPromedioOrden(promedio);
        } else {
            stats.setPromedioOrden(BigDecimal.ZERO);
        }
        
        return stats;
    }

    private OrdenCompraDto convertToDto(OrdenCompra orden) {
        OrdenCompraDto dto = new OrdenCompraDto();
        dto.setId(orden.getId());
        dto.setNumeroOrden(orden.getNumeroOrden());
        dto.setProveedorId(orden.getProveedorId());
        dto.setBodegaId(orden.getBodegaId());
        dto.setFechaCreacion(orden.getFechaCreacion());
        dto.setFechaAprobacion(orden.getFechaAprobacion());
        dto.setFechaRecepcion(orden.getFechaRecepcion());
        dto.setEstado(orden.getEstado());
        dto.setTotal(orden.getTotal());
        dto.setObservaciones(orden.getObservaciones());

        // Obtener nombre del proveedor
        try {
            ProveedorDto proveedor = proveedorClient.getProveedorById(orden.getProveedorId());
            if (proveedor != null) {
                dto.setProveedorNombre(proveedor.getNombre());
            }
        } catch (Exception e) {
            dto.setProveedorNombre("Proveedor " + orden.getProveedorId());
        }

        // Convertir detalles
        List<DetalleOrdenCompraDto> detallesDto = orden.getDetalles().stream()
                .map(this::convertDetalleToDto)
                .collect(Collectors.toList());
        dto.setDetalles(detallesDto);

        return dto;
    }

    private DetalleOrdenCompraDto convertDetalleToDto(DetalleOrdenCompra detalle) {
        DetalleOrdenCompraDto dto = new DetalleOrdenCompraDto();
        dto.setId(detalle.getId());
        dto.setOrdenCompraId(detalle.getOrdenCompraId());
        dto.setProductoId(detalle.getProductoId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        // Obtener nombre del producto
        try {
            ProductoDto producto = productoClient.getProductoById(detalle.getProductoId());
            if (producto != null) {
                dto.setProductoNombre(producto.getNombre());
            }
        } catch (Exception e) {
            dto.setProductoNombre("Producto " + detalle.getProductoId());
        }

        return dto;
    }

    private String generarNumeroOrden() {
        long count = ordenCompraRepository.count();
        return String.format("OC-%06d", count + 1);
    }
}
