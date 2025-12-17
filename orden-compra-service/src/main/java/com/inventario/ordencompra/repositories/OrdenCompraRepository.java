package com.inventario.ordencompra.repositories;

import com.inventario.ordencompra.entities.EstadoOrden;
import com.inventario.ordencompra.entities.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    
    List<OrdenCompra> findByEstado(EstadoOrden estado);
    
    List<OrdenCompra> findByProveedorId(Long proveedorId);
    
    List<OrdenCompra> findByBodegaId(Long bodegaId);
    
    @Query("SELECT o FROM OrdenCompra o WHERE o.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY o.fechaCreacion DESC")
    List<OrdenCompra> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT o FROM OrdenCompra o WHERE o.estado = :estado AND o.proveedorId = :proveedorId")
    List<OrdenCompra> findByEstadoAndProveedorId(@Param("estado") EstadoOrden estado, 
                                                   @Param("proveedorId") Long proveedorId);
}
