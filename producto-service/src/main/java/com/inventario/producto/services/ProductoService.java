package com.inventario.producto.services;

import com.inventario.producto.dto.ProductoDto;

import java.util.List;

public interface ProductoService {
    
    List<ProductoDto> findAll();
    
    ProductoDto findById(Long id);
    
    ProductoDto save(ProductoDto productoDto);
    
    ProductoDto update(Long id, ProductoDto productoDto);
    
    void delete(Long id);
    
    List<ProductoDto> findProductosCriticos();
    
    List<ProductoDto> findProductosBajoStock();
}
