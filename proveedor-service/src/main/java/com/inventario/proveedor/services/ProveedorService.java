package com.inventario.proveedor.services;

import com.inventario.proveedor.dto.ProveedorDto;
import com.inventario.proveedor.models.entities.Proveedor;

import java.util.List;

public interface ProveedorService {
    
    List<ProveedorDto> findAll();
    
    ProveedorDto findById(Long id);
    
    ProveedorDto save(ProveedorDto proveedorDto);
    
    ProveedorDto update(Long id, ProveedorDto proveedorDto);
    
    void deleteById(Long id);
    
    ProveedorDto findByRuc(String ruc);
    
    List<ProveedorDto> findByPais(String pais);
}
