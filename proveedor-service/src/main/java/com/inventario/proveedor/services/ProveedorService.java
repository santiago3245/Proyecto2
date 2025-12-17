package com.inventario.proveedor.services;

import com.inventario.proveedor.models.entities.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> findAll();
    Optional<Proveedor> findById(Long id);
    Optional<Proveedor> findByRuc(String ruc);
    List<Proveedor> findByPais(String pais);
    List<Proveedor> findByEstado(String estado);
    Proveedor save(Proveedor proveedor);
    Proveedor update(Long id, Proveedor proveedor);
    void deleteById(Long id);
    boolean existsByRuc(String ruc);
}
