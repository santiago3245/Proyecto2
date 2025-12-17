package com.inventario.producto.repositories;

import com.inventario.producto.models.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigo(String codigo);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByEstado(String estado);
    boolean existsByCodigo(String codigo);
    List<Producto> findByEstadoAndStockMinimoGreaterThan(String estado, Integer stockMinimo);
}
