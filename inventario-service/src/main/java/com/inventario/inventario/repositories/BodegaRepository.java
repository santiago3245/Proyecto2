package com.inventario.inventario.repositories;

import com.inventario.inventario.models.entities.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    
    Optional<Bodega> findByCodigo(String codigo);
    
    boolean existsByCodigo(String codigo);
}
