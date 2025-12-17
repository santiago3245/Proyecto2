package com.inventario.ordencompra.dto;

public class BodegaCapacidadDto {
    private Long bodegaId;
    private String bodegaNombre;
    private Integer capacidadMaxima;
    private String unidadCapacidad;
    private Integer cantidadProductos;
    private Double porcentajeUtilizacion;
    private String nivelAlerta;

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

    public String getUnidadCapacidad() {
        return unidadCapacidad;
    }

    public void setUnidadCapacidad(String unidadCapacidad) {
        this.unidadCapacidad = unidadCapacidad;
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

    public String getNivelAlerta() {
        return nivelAlerta;
    }

    public void setNivelAlerta(String nivelAlerta) {
        this.nivelAlerta = nivelAlerta;
    }
}
