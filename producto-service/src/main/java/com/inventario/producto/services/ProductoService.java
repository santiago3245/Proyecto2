package com.inventario.producto.services;

import com.inventario.producto.models.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    Optional<Producto> findByCodigo(String codigo);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByEstado(String estado);
    Producto save(Producto producto);
    Producto update(Long id, Producto producto);
    void deleteById(Long id);
    boolean existsByCodigo(String codigo);
    
    // Métodos para reportes
    List<Producto> findProductosCriticos();
    List<Producto> findProductosBajoStock();
}
