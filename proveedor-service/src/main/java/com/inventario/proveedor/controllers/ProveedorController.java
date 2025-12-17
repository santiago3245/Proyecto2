package com.inventario.proveedor.controllers;

import com.inventario.proveedor.dto.ProveedorDto;
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
    public ResponseEntity<List<ProveedorDto>> getAllProveedores() {
        List<ProveedorDto> proveedores = proveedorService.findAll();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDto> getProveedorById(@PathVariable Long id) {
        ProveedorDto proveedor = proveedorService.findById(id);
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    public ResponseEntity<ProveedorDto> createProveedor(@Valid @RequestBody ProveedorDto proveedorDto) {
        ProveedorDto savedProveedor = proveedorService.save(proveedorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDto> updateProveedor(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorDto proveedorDto) {
        ProveedorDto updatedProveedor = proveedorService.update(id, proveedorDto);
        return ResponseEntity.ok(updatedProveedor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<ProveedorDto> getProveedorByRuc(@PathVariable String ruc) {
        ProveedorDto proveedor = proveedorService.findByRuc(ruc);
        return ResponseEntity.ok(proveedor);
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<ProveedorDto>> getProveedoresByPais(@PathVariable String pais) {
        List<ProveedorDto> proveedores = proveedorService.findByPais(pais);
        return ResponseEntity.ok(proveedores);
    }
}
