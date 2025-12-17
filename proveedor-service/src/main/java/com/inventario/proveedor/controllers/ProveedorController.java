package com.inventario.proveedor.controllers;

import com.inventario.proveedor.models.entities.Proveedor;
import com.inventario.proveedor.services.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> getAllProveedores() {
        List<Proveedor> proveedores = proveedorService.findAll();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Long id) {
        return proveedorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Proveedor> getProveedorByRuc(@PathVariable String ruc) {
        return proveedorService.findByRuc(ruc)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<Proveedor>> getProveedoresByPais(@PathVariable String pais) {
        List<Proveedor> proveedores = proveedorService.findByPais(pais);
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Proveedor>> getProveedoresByEstado(@PathVariable String estado) {
        List<Proveedor> proveedores = proveedorService.findByEstado(estado);
        return ResponseEntity.ok(proveedores);
    }

    @PostMapping
    public ResponseEntity<?> createProveedor(@Valid @RequestBody Proveedor proveedor) {
        if (proveedorService.existsByRuc(proveedor.getRuc())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Proveedor con RUC '" + proveedor.getRuc() + "' ya existe");
        }
        Proveedor savedProveedor = proveedorService.save(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProveedor(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        try {
            Proveedor updatedProveedor = proveedorService.update(id, proveedor);
            return ResponseEntity.ok(updatedProveedor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        if (proveedorService.findById(id).isPresent()) {
            proveedorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/{ruc}")
    public ResponseEntity<Boolean> existsByRuc(@PathVariable String ruc) {
        boolean exists = proveedorService.existsByRuc(ruc);
        return ResponseEntity.ok(exists);
    }
}
