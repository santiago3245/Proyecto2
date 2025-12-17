package com.inventario.inventario.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser mayor a 0")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull(message = "El ID de la bodega es obligatorio")
    @Positive(message = "El ID de la bodega debe ser mayor a 0")
    @Column(name = "bodega_id", nullable = false)
    private Long bodegaId;

    @Size(max = 300, message = "La ubicación no puede exceder 300 caracteres")
    private String ubicacion;

    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible = 0;

    @Min(value = 0, message = "La cantidad reservada no puede ser negativa")
    @Column(name = "cantidad_reservada")
    private Integer cantidadReservada = 0;

    @Column(name = "fecha_ultima_entrada")
    private LocalDateTime fechaUltimaEntrada;

    @Column(name = "fecha_ultima_salida")
    private LocalDateTime fechaUltimaSalida;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaActualizacion = LocalDateTime.now();
        if (cantidadDisponible == null) {
            cantidadDisponible = 0;
        }
        if (cantidadReservada == null) {
            cantidadReservada = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getCantidadReservada() {
        return cantidadReservada;
    }

    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    public LocalDateTime getFechaUltimaEntrada() {
        return fechaUltimaEntrada;
    }

    public void setFechaUltimaEntrada(LocalDateTime fechaUltimaEntrada) {
        this.fechaUltimaEntrada = fechaUltimaEntrada;
    }

    public LocalDateTime getFechaUltimaSalida() {
        return fechaUltimaSalida;
    }

    public void setFechaUltimaSalida(LocalDateTime fechaUltimaSalida) {
        this.fechaUltimaSalida = fechaUltimaSalida;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
