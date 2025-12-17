package com.inventario.producto.controllers;

import com.inventario.producto.models.entities.Producto;
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
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Producto> getProductoByCodigo(@PathVariable String codigo) {
        return productoService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> getProductosByCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoService.findByCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> getProductosByEstado(@PathVariable String estado) {
        List<Producto> productos = productoService.findByEstado(estado);
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<?> createProducto(@Valid @RequestBody Producto producto) {
        if (productoService.existsByCodigo(producto.getCodigo())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Producto con código '" + producto.getCodigo() + "' ya existe");
        }
        Producto savedProducto = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        try {
            Producto updatedProducto = productoService.update(id, producto);
            return ResponseEntity.ok(updatedProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.findById(id).isPresent()) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/{codigo}")
    public ResponseEntity<Boolean> existsByCodigo(@PathVariable String codigo) {
        boolean exists = productoService.existsByCodigo(codigo);
        return ResponseEntity.ok(exists);
    }

    // ==================== ENDPOINTS DE REPORTES ====================
    
    @GetMapping("/reportes/criticos")
    public ResponseEntity<List<Producto>> getProductosCriticos() {
        List<Producto> productosCriticos = productoService.findProductosCriticos();
        return ResponseEntity.ok(productosCriticos);
    }

    @GetMapping("/reportes/bajo-stock")
    public ResponseEntity<List<Producto>> getProductosBajoStock() {
        List<Producto> productosBajoStock = productoService.findProductosBajoStock();
        return ResponseEntity.ok(productosBajoStock);
    }
}
