package com.inventario.ordencompra.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroOrden;

    @Column(nullable = false)
    private Long proveedorId;

    @Column(nullable = false)
    private Long bodegaId;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaAprobacion;

    private LocalDateTime fechaRecepcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(length = 1000)
    private String observaciones;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrdenCompra> detalles = new ArrayList<>();

    public OrdenCompra() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.total = BigDecimal.ZERO;
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

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
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

    public List<DetalleOrdenCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenCompra> detalles) {
        this.detalles = detalles;
    }

    public void addDetalle(DetalleOrdenCompra detalle) {
        detalles.add(detalle);
        detalle.setOrdenCompra(this);
    }

    public void removeDetalle(DetalleOrdenCompra detalle) {
        detalles.remove(detalle);
        detalle.setOrdenCompra(null);
    }

    public void calcularTotal() {
        this.total = detalles.stream()
                .map(DetalleOrdenCompra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
