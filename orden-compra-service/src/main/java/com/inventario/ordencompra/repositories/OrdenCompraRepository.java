package com.inventario.ordencompra.repositories;

import com.inventario.ordencompra.models.entities.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    Optional<OrdenCompra> findByNumeroOrden(String numeroOrden);
    List<OrdenCompra> findByProveedorId(Long proveedorId);
    List<OrdenCompra> findByEstado(String estado);
    boolean existsByNumeroOrden(String numeroOrden);
    List<OrdenCompra> findAllByOrderByFechaOrdenDesc();
}
