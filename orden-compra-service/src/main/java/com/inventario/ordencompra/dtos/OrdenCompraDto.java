package com.inventario.ordencompra.dtos;

import com.inventario.ordencompra.entities.EstadoOrden;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenCompraDto {
    
    private Long id;
    private String numeroOrden;
    private Long proveedorId;
    private String proveedorNombre;
    private Long bodegaId;
    private String bodegaNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaRecepcion;
    private EstadoOrden estado;
    private BigDecimal total;
    private String observaciones;
    private List<DetalleOrdenCompraDto> detalles;

    public OrdenCompraDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public String getBodegaNombre() {
        return bodegaNombre;
    }

    public void setBodegaNombre(String bodegaNombre) {
        this.bodegaNombre = bodegaNombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleOrdenCompraDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenCompraDto> detalles) {
        this.detalles = detalles;
    }
}
