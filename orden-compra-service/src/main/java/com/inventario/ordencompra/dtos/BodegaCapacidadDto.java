package com.inventario.ordencompra.dtos;

public class BodegaCapacidadDto {
    
    private Long bodegaId;
    private String bodegaNombre;
    private Integer capacidadMaxima;
    private Integer cantidadProductos;
    private Double porcentajeUtilizacion;

    public BodegaCapacidadDto() {
    }

    // Getters and Setters
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

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public Double getPorcentajeUtilizacion() {
        return porcentajeUtilizacion;
    }

    public void setPorcentajeUtilizacion(Double porcentajeUtilizacion) {
        this.porcentajeUtilizacion = porcentajeUtilizacion;
    }
}
