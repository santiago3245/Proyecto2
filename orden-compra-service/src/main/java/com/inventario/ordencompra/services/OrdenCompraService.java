package com.inventario.ordencompra.services;

import com.inventario.ordencompra.dtos.*;
import com.inventario.ordencompra.entities.EstadoOrden;
import com.inventario.ordencompra.entities.OrdenCompra;

import java.util.List;

public interface OrdenCompraService {
    
    List<OrdenCompraDto> findAll();
    
    OrdenCompraDto findById(Long id);
    
    OrdenCompraDto create(CrearOrdenRequest request);
    
    OrdenCompraDto update(Long id, CrearOrdenRequest request);
    
    OrdenCompraDto aprobarOrden(Long id);
    
    OrdenCompraDto recibirOrden(Long id);
    
    OrdenCompraDto cancelarOrden(Long id);
    
    List<OrdenCompraDto> findByEstado(EstadoOrden estado);
    
    List<OrdenCompraDto> findByProveedorId(Long proveedorId);
    
    List<OrdenCompraDto> obtenerHistorial();
    
    List<GastoPorProveedorDto> obtenerGastosPorProveedor();
    
    EstadisticasDto obtenerEstadisticas();
}
