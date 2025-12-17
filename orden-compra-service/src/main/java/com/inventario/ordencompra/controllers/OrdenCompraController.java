package com.inventario.ordencompra.controllers;

import com.inventario.ordencompra.clients.ProductoClientRest;
import com.inventario.ordencompra.clients.ProveedorClientRest;
import com.inventario.ordencompra.dto.ProductoDto;
import com.inventario.ordencompra.dto.ProveedorDto;
import com.inventario.ordencompra.models.entities.DetalleOrdenCompra;
import com.inventario.ordencompra.models.entities.OrdenCompra;
import com.inventario.ordencompra.services.OrdenCompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes-compra")
@CrossOrigin(origins = "*")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;

    @Autowired
    private ProductoClientRest productoClientRest;

    @Autowired
    private ProveedorClientRest proveedorClientRest;

    @GetMapping
    public ResponseEntity<List<OrdenCompra>> getAllOrdenesCompra() {
        List<OrdenCompra> ordenes = ordenCompraService.findAll();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> getOrdenCompraById(@PathVariable Long id) {
        return ordenCompraService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<OrdenCompra> getOrdenCompraByNumero(@PathVariable String numeroOrden) {
        return ordenCompraService.findByNumeroOrden(numeroOrden)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<OrdenCompra>> getOrdenesCompraByProveedor(@PathVariable Long proveedorId) {
        List<OrdenCompra> ordenes = ordenCompraService.findByProveedorId(proveedorId);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenCompra>> getOrdenesCompraByEstado(@PathVariable String estado) {
        List<OrdenCompra> ordenes = ordenCompraService.findByEstado(estado);
        return ResponseEntity.ok(ordenes);
    }

    @PostMapping
    public ResponseEntity<?> createOrdenCompra(@Valid @RequestBody OrdenCompra ordenCompra) {
        // Verificar que el proveedor existe
        Optional<ProveedorDto> proveedorOpt = proveedorClientRest.findById(ordenCompra.getProveedorId());
        if (!proveedorOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Proveedor no encontrado con ID: " + ordenCompra.getProveedorId());
        }

        // Verificar que todos los productos existen
        for (DetalleOrdenCompra detalle : ordenCompra.getDetalles()) {
            Optional<ProductoDto> productoOpt = productoClientRest.findById(detalle.getProductoId());
            if (!productoOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Producto no encontrado con ID: " + detalle.getProductoId());
            }
            
            // Si no se especificó precio, usar el del producto
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(productoOpt.get().getPrecio());
            }
        }

        // Verificar que no exista el número de orden
        if (ordenCompraService.existsByNumeroOrden(ordenCompra.getNumeroOrden())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Orden de compra con número '" + ordenCompra.getNumeroOrden() + "' ya existe");
        }

        OrdenCompra savedOrden = ordenCompraService.save(ordenCompra);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrden);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrdenCompra(@PathVariable Long id, @Valid @RequestBody OrdenCompra ordenCompra) {
        // Verificar que el proveedor existe
        Optional<ProveedorDto> proveedorOpt = proveedorClientRest.findById(ordenCompra.getProveedorId());
        if (!proveedorOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Proveedor no encontrado con ID: " + ordenCompra.getProveedorId());
        }

        // Verificar que todos los productos existen
        for (DetalleOrdenCompra detalle : ordenCompra.getDetalles()) {
            Optional<ProductoDto> productoOpt = productoClientRest.findById(detalle.getProductoId());
            if (!productoOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Producto no encontrado con ID: " + detalle.getProductoId());
            }
        }

        try {
            OrdenCompra updatedOrden = ordenCompraService.update(id, ordenCompra);
            return ResponseEntity.ok(updatedOrden);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrdenCompra(@PathVariable Long id) {
        if (ordenCompraService.findById(id).isPresent()) {
            ordenCompraService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarOrden(@PathVariable Long id) {
        try {
            OrdenCompra orden = ordenCompraService.aprobarOrden(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/recibir")
    public ResponseEntity<?> recibirOrden(@PathVariable Long id) {
        try {
            OrdenCompra orden = ordenCompraService.recibirOrden(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarOrden(@PathVariable Long id) {
        try {
            OrdenCompra orden = ordenCompraService.cancelarOrden(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== ENDPOINTS DE REPORTES ====================
    
    @GetMapping("/reportes/historial")
    public ResponseEntity<List<OrdenCompra>> getHistorialOrdenes() {
        List<OrdenCompra> ordenes = ordenCompraService.findAllOrdenesOrdenadas();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/reportes/gastos-por-proveedor")
    public ResponseEntity<?> getGastosPorProveedor() {
        return ResponseEntity.ok(ordenCompraService.calcularGastosPorProveedor());
    }

    @GetMapping("/reportes/estadisticas")
    public ResponseEntity<?> getEstadisticas() {
        return ResponseEntity.ok(ordenCompraService.obtenerEstadisticas());
    }
}
