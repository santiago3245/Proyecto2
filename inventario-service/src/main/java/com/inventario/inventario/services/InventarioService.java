package com.inventario.inventario.services;

import com.inventario.inventario.clients.ProductoClientRest;
import com.inventario.inventario.dto.AgregarStockRequest;
import com.inventario.inventario.dto.InventarioDto;
import com.inventario.inventario.dto.ProductoDto;
import com.inventario.inventario.models.entities.Bodega;
import com.inventario.inventario.models.entities.Inventario;
import com.inventario.inventario.repositories.BodegaRepository;
import com.inventario.inventario.repositories.InventarioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoClientRest productoClient;

    @Transactional(readOnly = true)
    public List<InventarioDto> findAll() {
        List<Inventario> inventarios = inventarioRepository.findAll();
        return inventarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventarioDto> findByProductoId(Long productoId) {
        List<Inventario> inventarios = inventarioRepository.findByProductoId(productoId);
        return inventarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventarioDto findById(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inventario no encontrado con ID: " + id));
        return convertToDto(inventario);
    }

    @Transactional
    public InventarioDto agregarStock(AgregarStockRequest request) {
        // Verificar que el producto existe
        ProductoDto producto = null;
        try {
            producto = productoClient.getProductoById(request.getProductoId());
        } catch (FeignException.NotFound e) {
            throw new NoSuchElementException("Producto no encontrado con ID: " + request.getProductoId());
        }

        // Verificar que la bodega existe
        Bodega bodega = bodegaRepository.findById(request.getBodegaId())
                .orElseThrow(() -> new NoSuchElementException("Bodega no encontrada con ID: " + request.getBodegaId()));

        // Buscar si ya existe inventario para este producto en esta bodega
        Inventario inventario = inventarioRepository
                .findByProductoIdAndBodegaId(request.getProductoId(), request.getBodegaId())
                .orElse(null);

        if (inventario == null) {
            // Crear nuevo registro de inventario
            inventario = new Inventario();
            inventario.setProductoId(request.getProductoId());
            inventario.setBodega(bodega);
            inventario.setUbicacion(request.getUbicacion());
            inventario.setCantidadDisponible(request.getCantidad());
            inventario.setFechaUltimaEntrada(LocalDateTime.now());
        } else {
            // Actualizar cantidad existente
            inventario.setCantidadDisponible(inventario.getCantidadDisponible() + request.getCantidad());
            inventario.setFechaUltimaEntrada(LocalDateTime.now());
            if (request.getUbicacion() != null && !request.getUbicacion().isEmpty()) {
                inventario.setUbicacion(request.getUbicacion());
            }
        }

        Inventario savedInventario = inventarioRepository.save(inventario);
        InventarioDto dto = convertToDto(savedInventario);
        dto.setProductoNombre(producto.getNombre());
        return dto;
    }

    @Transactional
    public InventarioDto update(Long id, InventarioDto inventarioDto) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inventario no encontrado con ID: " + id));

        // Verificar si cambió la bodega
        if (!inventario.getBodega().getId().equals(inventarioDto.getBodegaId())) {
            Bodega nuevaBodega = bodegaRepository.findById(inventarioDto.getBodegaId())
                    .orElseThrow(() -> new NoSuchElementException("Bodega no encontrada con ID: " + inventarioDto.getBodegaId()));
            inventario.setBodega(nuevaBodega);
        }

        if (inventarioDto.getUbicacion() != null) {
            inventario.setUbicacion(inventarioDto.getUbicacion());
        }

        if (inventarioDto.getCantidadDisponible() != null) {
            // Si la cantidad cambió, actualizar fecha correspondiente
            if (inventarioDto.getCantidadDisponible() > inventario.getCantidadDisponible()) {
                inventario.setFechaUltimaEntrada(LocalDateTime.now());
            } else if (inventarioDto.getCantidadDisponible() < inventario.getCantidadDisponible()) {
                inventario.setFechaUltimaSalida(LocalDateTime.now());
            }
            inventario.setCantidadDisponible(inventarioDto.getCantidadDisponible());
        }

        Inventario updatedInventario = inventarioRepository.save(inventario);
        return convertToDto(updatedInventario);
    }

    private InventarioDto convertToDto(Inventario inventario) {
        InventarioDto dto = new InventarioDto(inventario);
        
        // Intentar obtener el nombre del producto
        try {
            ProductoDto producto = productoClient.getProductoById(inventario.getProductoId());
            dto.setProductoNombre(producto.getNombre());
        } catch (Exception e) {
            dto.setProductoNombre("Producto ID: " + inventario.getProductoId());
        }
        
        return dto;
    }
}
