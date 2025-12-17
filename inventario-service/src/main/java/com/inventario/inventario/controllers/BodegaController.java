package com.inventario.inventario.controllers;

import com.inventario.inventario.dto.BodegaCapacidadDto;
import com.inventario.inventario.models.entities.Bodega;
import com.inventario.inventario.services.BodegaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
@CrossOrigin(origins = "*")
public class BodegaController {

    @Autowired
    private BodegaService bodegaService;

    @GetMapping
    public ResponseEntity<List<Bodega>> getAllBodegas() {
        List<Bodega> bodegas = bodegaService.findAll();
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bodega> getBodegaById(@PathVariable Long id) {
        return bodegaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Bodega> getBodegaByCodigo(@PathVariable String codigo) {
        return bodegaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Bodega>> getBodegasByEstado(@PathVariable String estado) {
        List<Bodega> bodegas = bodegaService.findByEstado(estado);
        return ResponseEntity.ok(bodegas);
    }

    @PostMapping
    public ResponseEntity<?> createBodega(@Valid @RequestBody Bodega bodega) {
        try {
            Bodega savedBodega = bodegaService.save(bodega);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBodega);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBodega(@PathVariable Long id, @Valid @RequestBody Bodega bodega) {
        try {
            Bodega updatedBodega = bodegaService.update(id, bodega);
            return ResponseEntity.ok(updatedBodega);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBodega(@PathVariable Long id) {
        try {
            bodegaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/capacidad")
    public ResponseEntity<List<BodegaCapacidadDto>> getCapacidadBodegas() {
        List<BodegaCapacidadDto> capacidades = bodegaService.getCapacidadBodegas();
        return ResponseEntity.ok(capacidades);
    }

    @GetMapping("/{id}/capacidad")
    public ResponseEntity<BodegaCapacidadDto> getCapacidadBodega(@PathVariable Long id) {
        return bodegaService.getCapacidadBodega(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
