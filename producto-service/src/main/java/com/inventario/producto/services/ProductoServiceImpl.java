package com.inventario.producto.services;

import com.inventario.producto.dto.ProductoDto;
import com.inventario.producto.models.entities.Producto;
import com.inventario.producto.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDto> findAll() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDto findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return convertToDto(producto);
    }

    @Override
    @Transactional
    public ProductoDto save(ProductoDto productoDto) {
        // Verificar si el código ya existe
        if (productoRepository.findByCodigo(productoDto.getCodigo()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con el código: " + productoDto.getCodigo());
        }
        
        Producto producto = convertToEntity(productoDto);
        Producto savedProducto = productoRepository.save(producto);
        return convertToDto(savedProducto);
    }

    @Override
    @Transactional
    public ProductoDto update(Long id, ProductoDto productoDto) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        
        // Verificar si el código ya existe en otro producto
        productoRepository.findByCodigo(productoDto.getCodigo())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new RuntimeException("Ya existe un producto con el código: " + productoDto.getCodigo());
                    }
                });
        
        existingProducto.setCodigo(productoDto.getCodigo());
        existingProducto.setNombre(productoDto.getNombre());
        existingProducto.setDescripcion(productoDto.getDescripcion());
        existingProducto.setCategoria(productoDto.getCategoria());
        existingProducto.setPrecio(productoDto.getPrecio());
        existingProducto.setUnidadMedida(productoDto.getUnidadMedida());
        existingProducto.setStockMinimo(productoDto.getStockMinimo());
        existingProducto.setEstado(productoDto.getEstado());
        
        Producto updatedProducto = productoRepository.save(existingProducto);
        return convertToDto(updatedProducto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDto> findProductosCriticos() {
        return productoRepository.findProductosCriticos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDto> findProductosBajoStock() {
        return productoRepository.findProductosBajoStock().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductoDto convertToDto(Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getUnidadMedida(),
                producto.getStockMinimo(),
                producto.getEstado()
        );
    }

    private Producto convertToEntity(ProductoDto dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setEstado(dto.getEstado() != null ? dto.getEstado() : Producto.EstadoProducto.ACTIVO);
        return producto;
    }
}
