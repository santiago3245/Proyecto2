package com.inventario.inventario.services;

import com.inventario.inventario.models.entities.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioService {
    List<Inventario> findAll();
    Optional<Inventario> findById(Long id);
    List<Inventario> findByProductoId(Long productoId);
    List<Inventario> findByBodegaId(Long bodegaId);
    Optional<Inventario> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
    Inventario save(Inventario inventario);
    Inventario update(Long id, Inventario inventario);
    void deleteById(Long id);
    Inventario agregarStock(Long productoId, Long bodegaId, Integer cantidad);
    Inventario retirarStock(Long productoId, Long bodegaId, Integer cantidad);
}
