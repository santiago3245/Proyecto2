package com.inventario.ordencompra.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de orden es obligatorio")
    @Size(max = 50, message = "El número de orden no puede exceder 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String numeroOrden;

    @NotNull(message = "El ID del proveedor es obligatorio")
    @Positive(message = "El ID del proveedor debe ser mayor a 0")
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @NotNull(message = "El ID de la bodega destino es obligatorio")
    @Positive(message = "El ID de la bodega debe ser mayor a 0")
    @Column(name = "bodega_id", nullable = false)
    private Long bodegaId;

    @Column(name = "fecha_orden")
    private LocalDateTime fechaOrden;

    @Column(name = "fecha_entrega_esperada")
    private LocalDateTime fechaEntregaEsperada;

    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(PENDIENTE|APROBADA|RECIBIDA|CANCELADA)$", 
             message = "El estado debe ser 'PENDIENTE', 'APROBADA', 'RECIBIDA' o 'CANCELADA'")
    @Column(length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "total")
    private Double total;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrdenCompra> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaOrden = LocalDateTime.now();
        if (estado == null) {
            estado = "PENDIENTE";
        }
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

    public LocalDateTime getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDateTime fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public LocalDateTime getFechaEntregaEsperada() {
        return fechaEntregaEsperada;
    }

    public void setFechaEntregaEsperada(LocalDateTime fechaEntregaEsperada) {
        this.fechaEntregaEsperada = fechaEntregaEsperada;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
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
}
