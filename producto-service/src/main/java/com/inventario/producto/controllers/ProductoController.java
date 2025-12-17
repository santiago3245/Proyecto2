package com.inventario.producto.controllers;

import com.inventario.producto.dto.ProductoDto;
import com.inventario.producto.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> findAll() {
        List<ProductoDto> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> findById(@PathVariable Long id) {
        ProductoDto producto = productoService.findById(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<ProductoDto> save(@Valid @RequestBody ProductoDto productoDto) {
        ProductoDto savedProducto = productoService.save(productoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> update(@PathVariable Long id, 
                                             @Valid @RequestBody ProductoDto productoDto) {
        ProductoDto updatedProducto = productoService.update(id, productoDto);
        return ResponseEntity.ok(updatedProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reportes/criticos")
    public ResponseEntity<List<ProductoDto>> findProductosCriticos() {
        List<ProductoDto> productos = productoService.findProductosCriticos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/reportes/bajo-stock")
    public ResponseEntity<List<ProductoDto>> findProductosBajoStock() {
        List<ProductoDto> productos = productoService.findProductosBajoStock();
        return ResponseEntity.ok(productos);
    }
}
