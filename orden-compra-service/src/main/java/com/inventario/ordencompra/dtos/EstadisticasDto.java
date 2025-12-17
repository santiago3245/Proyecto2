package com.inventario.ordencompra.dtos;

import java.math.BigDecimal;

public class EstadisticasDto {
    
    private Long totalOrdenes;
    private Long ordenesPendientes;
    private Long ordenesAprobadas;
    private Long ordenesRecibidas;
    private Long ordenesCanceladas;
    private BigDecimal totalGastado;
    private BigDecimal promedioOrden;

    public EstadisticasDto() {
    }

    // Getters and Setters
    public Long getTotalOrdenes() {
        return totalOrdenes;
    }

    public void setTotalOrdenes(Long totalOrdenes) {
        this.totalOrdenes = totalOrdenes;
    }

    public Long getOrdenesPendientes() {
        return ordenesPendientes;
    }

    public void setOrdenesPendientes(Long ordenesPendientes) {
        this.ordenesPendientes = ordenesPendientes;
    }

    public Long getOrdenesAprobadas() {
        return ordenesAprobadas;
    }

    public void setOrdenesAprobadas(Long ordenesAprobadas) {
        this.ordenesAprobadas = ordenesAprobadas;
    }

    public Long getOrdenesRecibidas() {
        return ordenesRecibidas;
    }

    public void setOrdenesRecibidas(Long ordenesRecibidas) {
        this.ordenesRecibidas = ordenesRecibidas;
    }

    public Long getOrdenesCanceladas() {
        return ordenesCanceladas;
    }

    public void setOrdenesCanceladas(Long ordenesCanceladas) {
        this.ordenesCanceladas = ordenesCanceladas;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }

    public BigDecimal getPromedioOrden() {
        return promedioOrden;
    }

    public void setPromedioOrden(BigDecimal promedioOrden) {
        this.promedioOrden = promedioOrden;
    }
}
