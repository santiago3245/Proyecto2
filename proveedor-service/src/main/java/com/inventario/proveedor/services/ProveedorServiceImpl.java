package com.inventario.proveedor.services;

import com.inventario.proveedor.models.entities.Proveedor;
import com.inventario.proveedor.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> findById(Long id) {
        return proveedorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> findByRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> findByPais(String pais) {
        return proveedorRepository.findByPais(pais);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> findByEstado(String estado) {
        return proveedorRepository.findByEstado(estado);
    }

    @Override
    @Transactional
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional
    public Proveedor update(Long id, Proveedor proveedor) {
        return proveedorRepository.findById(id)
                .map(existingProveedor -> {
                    existingProveedor.setRuc(proveedor.getRuc());
                    existingProveedor.setRazonSocial(proveedor.getRazonSocial());
                    existingProveedor.setNombreComercial(proveedor.getNombreComercial());
                    existingProveedor.setPais(proveedor.getPais());
                    existingProveedor.setDireccion(proveedor.getDireccion());
                    existingProveedor.setTelefono(proveedor.getTelefono());
                    existingProveedor.setEmail(proveedor.getEmail());
                    existingProveedor.setContacto(proveedor.getContacto());
                    existingProveedor.setEstado(proveedor.getEstado());
                    return proveedorRepository.save(existingProveedor);
                })
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRuc(String ruc) {
        return proveedorRepository.existsByRuc(ruc);
    }
}
