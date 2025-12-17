package com.inventario.producto.repositories;

import com.inventario.producto.models.entities.Producto;
import com.inventario.producto.models.entities.Producto.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByCodigo(String codigo);
    
    List<Producto> findByEstado(EstadoProducto estado);
    
    @Query("SELECT p FROM Producto p WHERE p.stockMinimo > 0")
    List<Producto> findProductosCriticos();
    
    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO'")
    List<Producto> findProductosBajoStock();
}
