package com.inventario.ordencompra.dtos;

import java.math.BigDecimal;

public class GastoPorProveedorDto {
    
    private Long proveedorId;
    private String proveedorNombre;
    private Long cantidadOrdenes;
    private BigDecimal totalGastado;

    public GastoPorProveedorDto() {
    }

    public GastoPorProveedorDto(Long proveedorId, String proveedorNombre, Long cantidadOrdenes, BigDecimal totalGastado) {
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
        this.cantidadOrdenes = cantidadOrdenes;
        this.totalGastado = totalGastado;
    }

    // Getters and Setters
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

    public Long getCantidadOrdenes() {
        return cantidadOrdenes;
    }

    public void setCantidadOrdenes(Long cantidadOrdenes) {
        this.cantidadOrdenes = cantidadOrdenes;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }
}
