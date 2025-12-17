package com.inventario.inventario.services;

import com.inventario.inventario.models.entities.Inventario;
import com.inventario.inventario.repositories.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> findByProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> findByBodegaId(Long bodegaId) {
        return inventarioRepository.findByBodegaId(bodegaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventario> findByProductoIdAndBodegaId(Long productoId, Long bodegaId) {
        return inventarioRepository.findByProductoIdAndBodegaId(productoId, bodegaId);
    }

    @Override
    @Transactional
    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    @Transactional
    public Inventario update(Long id, Inventario inventario) {
        return inventarioRepository.findById(id)
                .map(existingInventario -> {
                    existingInventario.setProductoId(inventario.getProductoId());
                    existingInventario.setBodegaId(inventario.getBodegaId());
                    existingInventario.setUbicacion(inventario.getUbicacion());
                    existingInventario.setCantidadDisponible(inventario.getCantidadDisponible());
                    existingInventario.setCantidadReservada(inventario.getCantidadReservada());
                    return inventarioRepository.save(existingInventario);
                })
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        inventarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Inventario agregarStock(Long productoId, Long bodegaId, Integer cantidad) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findByProductoIdAndBodegaId(productoId, bodegaId);
        
        Inventario inventario;
        if (inventarioOpt.isPresent()) {
            inventario = inventarioOpt.get();
            inventario.setCantidadDisponible(inventario.getCantidadDisponible() + cantidad);
        } else {
            inventario = new Inventario();
            inventario.setProductoId(productoId);
            inventario.setBodegaId(bodegaId);
            inventario.setCantidadDisponible(cantidad);
            inventario.setCantidadReservada(0);
            inventario.setUbicacion("RECEPCION"); // Ubicación por defecto para nuevos ingresos
        }
        
        inventario.setFechaUltimaEntrada(LocalDateTime.now());
        return inventarioRepository.save(inventario);
    }

    @Override
    @Transactional
    public Inventario retirarStock(Long productoId, Long bodegaId, Integer cantidad) {
        Inventario inventario = inventarioRepository.findByProductoIdAndBodegaId(productoId, bodegaId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        if (inventario.getCantidadDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidad);
        inventario.setFechaUltimaSalida(LocalDateTime.now());
        return inventarioRepository.save(inventario);
    }
}
