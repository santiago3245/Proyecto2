package com.inventario.proveedor.repositories;

import com.inventario.proveedor.models.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    Optional<Proveedor> findByRuc(String ruc);
    
    List<Proveedor> findByPais(String pais);
}
