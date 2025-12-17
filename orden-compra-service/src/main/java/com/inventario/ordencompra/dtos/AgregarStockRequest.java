package com.inventario.ordencompra.dtos;

public class AgregarStockRequest {
    
    private Long bodegaId;
    private Long productoId;
    private Integer cantidad;
    private String motivo;

    public AgregarStockRequest() {
    }

    public AgregarStockRequest(Long bodegaId, Long productoId, Integer cantidad, String motivo) {
        this.bodegaId = bodegaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    // Getters and Setters
    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
