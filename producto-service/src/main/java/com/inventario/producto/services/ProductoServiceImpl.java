package com.inventario.producto.services;

import com.inventario.producto.models.entities.Producto;
import com.inventario.producto.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findByCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByEstado(String estado) {
        return productoRepository.findByEstado(estado);
    }

    @Override
    @Transactional
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto update(Long id, Producto producto) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setCodigo(producto.getCodigo());
                    existingProducto.setNombre(producto.getNombre());
                    existingProducto.setDescripcion(producto.getDescripcion());
                    existingProducto.setCategoria(producto.getCategoria());
                    existingProducto.setPrecio(producto.getPrecio());
                    existingProducto.setUnidadMedida(producto.getUnidadMedida());
                    existingProducto.setStockMinimo(producto.getStockMinimo());
                    existingProducto.setEstado(producto.getEstado());
                    return productoRepository.save(existingProducto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigo(String codigo) {
        return productoRepository.existsByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findProductosCriticos() {
        // Productos activos que tienen stock mínimo definido (para incluirlos en reportes)
        return productoRepository.findByEstadoAndStockMinimoGreaterThan("ACTIVO", 0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findProductosBajoStock() {
        // Retorna todos los productos activos (el cálculo real de stock se hará en el frontend con inventario)
        return productoRepository.findByEstado("ACTIVO");
    }
}
