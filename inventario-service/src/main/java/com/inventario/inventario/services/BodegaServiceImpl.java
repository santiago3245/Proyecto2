package com.inventario.inventario.services;

import com.inventario.inventario.dto.BodegaCapacidadDto;
import com.inventario.inventario.models.entities.Bodega;
import com.inventario.inventario.repositories.BodegaRepository;
import com.inventario.inventario.repositories.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BodegaServiceImpl implements BodegaService {

    @Autowired
    private BodegaRepository bodegaRepository;
    
    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> findAll() {
        return bodegaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bodega> findById(Long id) {
        return bodegaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bodega> findByCodigo(String codigo) {
        return bodegaRepository.findByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> findByEstado(String estado) {
        return bodegaRepository.findByEstado(estado);
    }

    @Override
    @Transactional
    public Bodega save(Bodega bodega) {
        if (bodegaRepository.existsByCodigo(bodega.getCodigo())) {
            throw new RuntimeException("Ya existe una bodega con el código: " + bodega.getCodigo());
        }
        return bodegaRepository.save(bodega);
    }

    @Override
    @Transactional
    public Bodega update(Long id, Bodega bodega) {
        return bodegaRepository.findById(id)
                .map(existingBodega -> {
                    existingBodega.setNombre(bodega.getNombre());
                    existingBodega.setDireccion(bodega.getDireccion());
                    existingBodega.setCiudad(bodega.getCiudad());
                    existingBodega.setTelefono(bodega.getTelefono());
                    existingBodega.setCapacidadMaxima(bodega.getCapacidadMaxima());
                    if (bodega.getUnidadCapacidad() != null) {
                        existingBodega.setUnidadCapacidad(bodega.getUnidadCapacidad());
                    }
                    existingBodega.setEstado(bodega.getEstado());
                    return bodegaRepository.save(existingBodega);
                })
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!bodegaRepository.existsById(id)) {
            throw new RuntimeException("Bodega no encontrada con id: " + id);
        }
        bodegaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BodegaCapacidadDto> getCapacidadBodegas() {
        return bodegaRepository.findAll().stream()
                .map(this::convertToCapacidadDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BodegaCapacidadDto> getCapacidadBodega(Long id) {
        return bodegaRepository.findById(id)
                .map(this::convertToCapacidadDto);
    }
    
    private BodegaCapacidadDto convertToCapacidadDto(Bodega bodega) {
        Integer cantidadTotal = inventarioRepository.sumCantidadDisponibleByBodegaId(bodega.getId());
        return new BodegaCapacidadDto(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getCapacidadMaxima(),
                bodega.getUnidadCapacidad(),
                cantidadTotal
        );
    }
}
