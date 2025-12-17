package com.inventario.inventario.controllers;

import com.inventario.inventario.dto.AgregarStockRequest;
import com.inventario.inventario.dto.InventarioDto;
import com.inventario.inventario.services.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioDto>> getAllInventario() {
        List<InventarioDto> inventarios = inventarioService.findAll();
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDto> getInventarioById(@PathVariable Long id) {
        InventarioDto inventario = inventarioService.findById(id);
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioDto>> getInventarioByProductoId(@PathVariable Long productoId) {
        List<InventarioDto> inventarios = inventarioService.findByProductoId(productoId);
        return ResponseEntity.ok(inventarios);
    }

    @PostMapping("/agregar-stock")
    public ResponseEntity<InventarioDto> agregarStock(@Valid @RequestBody AgregarStockRequest request) {
        InventarioDto inventario = inventarioService.agregarStock(request);
        return new ResponseEntity<>(inventario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDto> updateInventario(
            @PathVariable Long id,
            @Valid @RequestBody InventarioDto inventarioDto) {
        InventarioDto updatedInventario = inventarioService.update(id, inventarioDto);
        return ResponseEntity.ok(updatedInventario);
    }
}
