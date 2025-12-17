package com.inventario.inventario.dto;

public class BodegaCapacidadDto {
    private Long bodegaId;
    private String bodegaNombre;
    private Integer capacidadMaxima;
    private String unidadCapacidad;
    private Integer cantidadProductos;
    private Double porcentajeUtilizacion;
    private String nivelAlerta; // VERDE, AMARILLO, ROJO

    public BodegaCapacidadDto() {
    }

    public BodegaCapacidadDto(Long bodegaId, String bodegaNombre, Integer capacidadMaxima, 
                              String unidadCapacidad, Integer cantidadProductos) {
        this.bodegaId = bodegaId;
        this.bodegaNombre = bodegaNombre;
        this.capacidadMaxima = capacidadMaxima;
        this.unidadCapacidad = unidadCapacidad;
        this.cantidadProductos = cantidadProductos;
        
        // Calcular porcentaje de utilización
        if (capacidadMaxima != null && capacidadMaxima > 0) {
            this.porcentajeUtilizacion = (cantidadProductos.doubleValue() / capacidadMaxima) * 100;
            
            // Determinar nivel de alerta
            if (porcentajeUtilizacion >= 90) {
                this.nivelAlerta = "ROJO";
            } else if (porcentajeUtilizacion >= 70) {
                this.nivelAlerta = "AMARILLO";
            } else {
                this.nivelAlerta = "VERDE";
            }
        } else {
            this.porcentajeUtilizacion = 0.0;
            this.nivelAlerta = "VERDE";
        }
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
