package com.inventario.inventario.controllers;

import com.inventario.inventario.clients.ProductoClientRest;
import com.inventario.inventario.dto.ProductoDto;
import com.inventario.inventario.models.entities.Inventario;
import com.inventario.inventario.services.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoClientRest productoClientRest;

    @GetMapping
    public ResponseEntity<List<Inventario>> getAllInventario() {
        List<Inventario> inventarios = inventarioService.findAll();
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getInventarioById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Inventario>> getInventarioByProductoId(@PathVariable Long productoId) {
        List<Inventario> inventarios = inventarioService.findByProductoId(productoId);
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/bodega")
    public ResponseEntity<List<Inventario>> getInventarioByBodega(@RequestParam Long bodegaId) {
        List<Inventario> inventarios = inventarioService.findByBodegaId(bodegaId);
        return ResponseEntity.ok(inventarios);
    }

    @PostMapping
    public ResponseEntity<?> createInventario(@Valid @RequestBody Inventario inventario) {
        // Verificar que el producto existe
        Optional<ProductoDto> productoOpt = productoClientRest.findById(inventario.getProductoId());
        if (!productoOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Producto no encontrado con ID: " + inventario.getProductoId());
        }
        
        Inventario savedInventario = inventarioService.save(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventario(@PathVariable Long id, @Valid @RequestBody Inventario inventario) {
        // Verificar que el producto existe
        Optional<ProductoDto> productoOpt = productoClientRest.findById(inventario.getProductoId());
        if (!productoOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Producto no encontrado con ID: " + inventario.getProductoId());
        }
        
        try {
            Inventario updatedInventario = inventarioService.update(id, inventario);
            return ResponseEntity.ok(updatedInventario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/agregar-stock")
    public ResponseEntity<?> agregarStock(@RequestParam Long productoId, 
                                          @RequestParam Long bodegaId, 
                                          @RequestParam Integer cantidad) {
        // Verificar que el producto existe
        Optional<ProductoDto> productoOpt = productoClientRest.findById(productoId);
        if (!productoOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Producto no encontrado con ID: " + productoId);
        }
        
        try {
            Inventario inventario = inventarioService.agregarStock(productoId, bodegaId, cantidad);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/retirar-stock")
    public ResponseEntity<?> retirarStock(@RequestParam Long productoId, 
                                          @RequestParam Long bodegaId, 
                                          @RequestParam Integer cantidad) {
        try {
            Inventario inventario = inventarioService.retirarStock(productoId, bodegaId, cantidad);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
