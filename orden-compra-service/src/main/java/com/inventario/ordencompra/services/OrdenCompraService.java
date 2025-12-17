package com.inventario.ordencompra.services;

import com.inventario.ordencompra.models.entities.OrdenCompra;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrdenCompraService {
    List<OrdenCompra> findAll();
    Optional<OrdenCompra> findById(Long id);
    Optional<OrdenCompra> findByNumeroOrden(String numeroOrden);
    List<OrdenCompra> findByProveedorId(Long proveedorId);
    List<OrdenCompra> findByEstado(String estado);
    OrdenCompra save(OrdenCompra ordenCompra);
    OrdenCompra update(Long id, OrdenCompra ordenCompra);
    void deleteById(Long id);
    boolean existsByNumeroOrden(String numeroOrden);
    OrdenCompra aprobarOrden(Long id);
    OrdenCompra recibirOrden(Long id);
    OrdenCompra cancelarOrden(Long id);
    
    // Métodos para reportes
    List<OrdenCompra> findAllOrdenesOrdenadas();
    List<Map<String, Object>> calcularGastosPorProveedor();
    Map<String, Object> obtenerEstadisticas();
}
