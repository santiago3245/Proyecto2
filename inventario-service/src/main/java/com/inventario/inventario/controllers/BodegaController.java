package com.inventario.inventario.controllers;

import com.inventario.inventario.dto.BodegaCapacidadDto;
import com.inventario.inventario.dto.BodegaDto;
import com.inventario.inventario.services.BodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @GetMapping
    public ResponseEntity<List<BodegaDto>> getAllBodegas() {
        List<BodegaDto> bodegas = bodegaService.findAll();
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaDto> getBodegaById(@PathVariable Long id) {
        BodegaDto bodega = bodegaService.findById(id);
        return ResponseEntity.ok(bodega);
    }

    @PostMapping
    public ResponseEntity<BodegaDto> createBodega(@Valid @RequestBody BodegaDto bodegaDto) {
        BodegaDto newBodega = bodegaService.save(bodegaDto);
        return new ResponseEntity<>(newBodega, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaDto> updateBodega(
            @PathVariable Long id,
            @Valid @RequestBody BodegaDto bodegaDto) {
        BodegaDto updatedBodega = bodegaService.update(id, bodegaDto);
        return ResponseEntity.ok(updatedBodega);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodega(@PathVariable Long id) {
        bodegaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/capacidad")
    public ResponseEntity<List<BodegaCapacidadDto>> getAllCapacidades() {
        List<BodegaCapacidadDto> capacidades = bodegaService.getAllCapacidades();
        return ResponseEntity.ok(capacidades);
    }

    @GetMapping("/{id}/capacidad")
    public ResponseEntity<BodegaCapacidadDto> getCapacidadById(@PathVariable Long id) {
        BodegaCapacidadDto capacidad = bodegaService.getCapacidadById(id);
        return ResponseEntity.ok(capacidad);
    }
}
