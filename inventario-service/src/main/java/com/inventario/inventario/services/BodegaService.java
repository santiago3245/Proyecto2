package com.inventario.inventario.services;

import com.inventario.inventario.dto.BodegaCapacidadDto;
import com.inventario.inventario.dto.BodegaDto;
import com.inventario.inventario.models.entities.Bodega;
import com.inventario.inventario.repositories.BodegaRepository;
import com.inventario.inventario.repositories.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BodegaService {

    private final BodegaRepository bodegaRepository;
    private final InventarioRepository inventarioRepository;

    @Transactional(readOnly = true)
    public List<BodegaDto> findAll() {
        return bodegaRepository.findAll().stream()
                .map(BodegaDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BodegaDto findById(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bodega no encontrada con ID: " + id));
        return new BodegaDto(bodega);
    }

    @Transactional
    public BodegaDto save(BodegaDto bodegaDto) {
        if (bodegaDto.getId() == null && bodegaRepository.existsByCodigo(bodegaDto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una bodega con el código: " + bodegaDto.getCodigo());
        }

        Bodega bodega = new Bodega();
        bodega.setId(bodegaDto.getId());
        bodega.setCodigo(bodegaDto.getCodigo());
        bodega.setNombre(bodegaDto.getNombre());
        bodega.setDireccion(bodegaDto.getDireccion());
        bodega.setCiudad(bodegaDto.getCiudad());
        bodega.setTelefono(bodegaDto.getTelefono());
        bodega.setCapacidadMaxima(bodegaDto.getCapacidadMaxima());
        bodega.setUnidadCapacidad(bodegaDto.getUnidadCapacidad());
        bodega.setEstado(bodegaDto.getEstado());

        Bodega savedBodega = bodegaRepository.save(bodega);
        return new BodegaDto(savedBodega);
    }

    @Transactional
    public BodegaDto update(Long id, BodegaDto bodegaDto) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bodega no encontrada con ID: " + id));

        // Verificar si el código cambió y si ya existe
        if (!bodega.getCodigo().equals(bodegaDto.getCodigo()) && 
            bodegaRepository.existsByCodigo(bodegaDto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una bodega con el código: " + bodegaDto.getCodigo());
        }

        bodega.setCodigo(bodegaDto.getCodigo());
        bodega.setNombre(bodegaDto.getNombre());
        bodega.setDireccion(bodegaDto.getDireccion());
        bodega.setCiudad(bodegaDto.getCiudad());
        bodega.setTelefono(bodegaDto.getTelefono());
        bodega.setCapacidadMaxima(bodegaDto.getCapacidadMaxima());
        bodega.setUnidadCapacidad(bodegaDto.getUnidadCapacidad());
        bodega.setEstado(bodegaDto.getEstado());

        Bodega updatedBodega = bodegaRepository.save(bodega);
        return new BodegaDto(updatedBodega);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bodegaRepository.existsById(id)) {
            throw new NoSuchElementException("Bodega no encontrada con ID: " + id);
        }
        
        // Verificar si tiene inventario
        List<?> inventarios = inventarioRepository.findByBodegaId(id);
        if (!inventarios.isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar la bodega porque tiene inventario asociado");
        }
        
        bodegaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BodegaCapacidadDto> getAllCapacidades() {
        List<Bodega> bodegas = bodegaRepository.findAll();
        return bodegas.stream()
                .map(this::calcularCapacidad)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BodegaCapacidadDto getCapacidadById(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bodega no encontrada con ID: " + id));
        return calcularCapacidad(bodega);
    }

    private BodegaCapacidadDto calcularCapacidad(Bodega bodega) {
        Integer cantidadTotal = inventarioRepository.sumCantidadByBodegaId(bodega.getId());
        if (cantidadTotal == null) {
            cantidadTotal = 0;
        }

        BigDecimal porcentaje = BigDecimal.ZERO;
        if (bodega.getCapacidadMaxima().compareTo(BigDecimal.ZERO) > 0) {
            porcentaje = new BigDecimal(cantidadTotal)
                    .divide(bodega.getCapacidadMaxima(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BodegaCapacidadDto.NivelAlerta nivelAlerta = determinarNivelAlerta(porcentaje);

        BodegaCapacidadDto capacidadDto = new BodegaCapacidadDto();
        capacidadDto.setBodegaId(bodega.getId());
        capacidadDto.setBodegaNombre(bodega.getNombre());
        capacidadDto.setCapacidadMaxima(bodega.getCapacidadMaxima());
        capacidadDto.setUnidadCapacidad(bodega.getUnidadCapacidad());
        capacidadDto.setCantidadProductos(cantidadTotal);
        capacidadDto.setPorcentajeUtilizacion(porcentaje);
        capacidadDto.setNivelAlerta(nivelAlerta);

        return capacidadDto;
    }

    private BodegaCapacidadDto.NivelAlerta determinarNivelAlerta(BigDecimal porcentaje) {
        if (porcentaje.compareTo(new BigDecimal("90")) >= 0) {
            return BodegaCapacidadDto.NivelAlerta.ROJO;
        } else if (porcentaje.compareTo(new BigDecimal("70")) >= 0) {
            return BodegaCapacidadDto.NivelAlerta.AMARILLO;
        } else {
            return BodegaCapacidadDto.NivelAlerta.VERDE;
        }
    }
}
