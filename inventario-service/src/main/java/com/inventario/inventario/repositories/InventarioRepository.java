package com.inventario.inventario.repositories;

import com.inventario.inventario.models.entities.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByProductoId(Long productoId);
    List<Inventario> findByBodegaId(Long bodegaId);
    Optional<Inventario> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
    
    @Query("SELECT COALESCE(SUM(i.cantidadDisponible), 0) FROM Inventario i WHERE i.bodegaId = :bodegaId")
    Integer sumCantidadDisponibleByBodegaId(@Param("bodegaId") Long bodegaId);
}
