package com.inventario.ordencompra.controllers;

import com.inventario.ordencompra.dtos.*;
import com.inventario.ordencompra.entities.EstadoOrden;
import com.inventario.ordencompra.services.OrdenCompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes-compra")
@CrossOrigin(origins = "*")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;

    @GetMapping
    public ResponseEntity<List<OrdenCompraDto>> getAllOrdenes() {
        List<OrdenCompraDto> ordenes = ordenCompraService.findAll();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraDto> getOrdenById(@PathVariable Long id) {
        try {
            OrdenCompraDto orden = ordenCompraService.findById(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<OrdenCompraDto> createOrden(@Valid @RequestBody CrearOrdenRequest request) {
        try {
            OrdenCompraDto orden = ordenCompraService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompraDto> updateOrden(@PathVariable Long id, 
                                                       @Valid @RequestBody CrearOrdenRequest request) {
        try {
            OrdenCompraDto orden = ordenCompraService.update(id, request);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarOrden(@PathVariable Long id) {
        try {
            OrdenCompraDto orden = ordenCompraService.aprobarOrden(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/recibir")
    public ResponseEntity<?> recibirOrden(@PathVariable Long id) {
        try {
            OrdenCompraDto orden = ordenCompraService.recibirOrden(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarOrden(@PathVariable Long id) {
        try {
            OrdenCompraDto orden = ordenCompraService.cancelarOrden(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenCompraDto>> getOrdenesByEstado(@PathVariable String estado) {
        try {
            EstadoOrden estadoOrden = EstadoOrden.valueOf(estado.toUpperCase());
            List<OrdenCompraDto> ordenes = ordenCompraService.findByEstado(estadoOrden);
            return ResponseEntity.ok(ordenes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<OrdenCompraDto>> getOrdenesByProveedor(@PathVariable Long proveedorId) {
        List<OrdenCompraDto> ordenes = ordenCompraService.findByProveedorId(proveedorId);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/reportes/historial")
    public ResponseEntity<List<OrdenCompraDto>> getHistorial() {
        List<OrdenCompraDto> historial = ordenCompraService.obtenerHistorial();
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/reportes/gastos-por-proveedor")
    public ResponseEntity<List<GastoPorProveedorDto>> getGastosPorProveedor() {
        List<GastoPorProveedorDto> gastos = ordenCompraService.obtenerGastosPorProveedor();
        return ResponseEntity.ok(gastos);
    }

    @GetMapping("/reportes/estadisticas")
    public ResponseEntity<EstadisticasDto> getEstadisticas() {
        EstadisticasDto stats = ordenCompraService.obtenerEstadisticas();
        return ResponseEntity.ok(stats);
    }
}
