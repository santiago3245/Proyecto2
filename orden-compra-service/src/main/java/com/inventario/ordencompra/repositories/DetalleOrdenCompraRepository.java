package com.inventario.ordencompra.repositories;

import com.inventario.ordencompra.entities.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Long> {
    
    List<DetalleOrdenCompra> findByOrdenCompraId(Long ordenCompraId);
    
    @Query("SELECT d FROM DetalleOrdenCompra d WHERE d.productoId = :productoId")
    List<DetalleOrdenCompra> findByProductoId(@Param("productoId") Long productoId);
}
