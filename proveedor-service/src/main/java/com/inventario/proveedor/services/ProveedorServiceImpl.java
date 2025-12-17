package com.inventario.proveedor.services;

import com.inventario.proveedor.dto.ProveedorDto;
import com.inventario.proveedor.models.entities.Proveedor;
import com.inventario.proveedor.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findAll() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDto findById(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
        return convertToDto(proveedor);
    }

    @Override
    @Transactional
    public ProveedorDto save(ProveedorDto proveedorDto) {
        if (proveedorRepository.findByRuc(proveedorDto.getRuc()).isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con el RUC: " + proveedorDto.getRuc());
        }
        Proveedor proveedor = convertToEntity(proveedorDto);
        Proveedor savedProveedor = proveedorRepository.save(proveedor);
        return convertToDto(savedProveedor);
    }

    @Override
    @Transactional
    public ProveedorDto update(Long id, ProveedorDto proveedorDto) {
        Proveedor existingProveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
        
        if (!existingProveedor.getRuc().equals(proveedorDto.getRuc()) 
            && proveedorRepository.findByRuc(proveedorDto.getRuc()).isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con el RUC: " + proveedorDto.getRuc());
        }
        
        existingProveedor.setRuc(proveedorDto.getRuc());
        existingProveedor.setRazonSocial(proveedorDto.getRazonSocial());
        existingProveedor.setNombreComercial(proveedorDto.getNombreComercial());
        existingProveedor.setDireccion(proveedorDto.getDireccion());
        existingProveedor.setCiudad(proveedorDto.getCiudad());
        existingProveedor.setPais(proveedorDto.getPais());
        existingProveedor.setTelefono(proveedorDto.getTelefono());
        existingProveedor.setEmail(proveedorDto.getEmail());
        existingProveedor.setSitioWeb(proveedorDto.getSitioWeb());
        existingProveedor.setCategoria(proveedorDto.getCategoria());
        existingProveedor.setEstado(proveedorDto.getEstado());
        
        Proveedor updatedProveedor = proveedorRepository.save(existingProveedor);
        return convertToDto(updatedProveedor);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        }
        proveedorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDto findByRuc(String ruc) {
        Proveedor proveedor = proveedorRepository.findByRuc(ruc)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con RUC: " + ruc));
        return convertToDto(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findByPais(String pais) {
        return proveedorRepository.findByPais(pais)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProveedorDto convertToDto(Proveedor proveedor) {
        return new ProveedorDto(
                proveedor.getId(),
                proveedor.getRuc(),
                proveedor.getRazonSocial(),
                proveedor.getNombreComercial(),
                proveedor.getDireccion(),
                proveedor.getCiudad(),
                proveedor.getPais(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getSitioWeb(),
                proveedor.getCategoria(),
                proveedor.getEstado()
        );
    }

    private Proveedor convertToEntity(ProveedorDto dto) {
        return new Proveedor(
                dto.getRuc(),
                dto.getRazonSocial(),
                dto.getNombreComercial(),
                dto.getDireccion(),
                dto.getCiudad(),
                dto.getPais(),
                dto.getTelefono(),
                dto.getEmail(),
                dto.getSitioWeb(),
                dto.getCategoria(),
                dto.getEstado() != null ? dto.getEstado() : Proveedor.EstadoProveedor.ACTIVO
        );
    }
}
