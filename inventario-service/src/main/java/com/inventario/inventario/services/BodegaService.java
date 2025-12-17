package com.inventario.inventario.services;

import com.inventario.inventario.dto.BodegaCapacidadDto;
import com.inventario.inventario.models.entities.Bodega;
import java.util.List;
import java.util.Optional;

public interface BodegaService {
    
    List<Bodega> findAll();
    
    Optional<Bodega> findById(Long id);
    
    Optional<Bodega> findByCodigo(String codigo);
    
    List<Bodega> findByEstado(String estado);
    
    Bodega save(Bodega bodega);
    
    Bodega update(Long id, Bodega bodega);
    
    void deleteById(Long id);
    
    List<BodegaCapacidadDto> getCapacidadBodegas();
    
    Optional<BodegaCapacidadDto> getCapacidadBodega(Long id);
}
