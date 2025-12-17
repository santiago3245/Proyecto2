package com.inventario.ordencompra.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CrearOrdenRequest {
    
    @NotNull(message = "El ID del proveedor es requerido")
    private Long proveedorId;
    
    @NotNull(message = "El ID de la bodega es requerido")
    private Long bodegaId;
    
    private String observaciones;
    
    @NotEmpty(message = "Debe incluir al menos un detalle")
    private List<DetalleRequest> detalles;

    public CrearOrdenRequest() {
    }

    // Getters and Setters
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleRequest> detalles) {
        this.detalles = detalles;
    }

    public static class DetalleRequest {
        @NotNull(message = "El ID del producto es requerido")
        private Long productoId;
        
        @NotNull(message = "La cantidad es requerida")
        private Integer cantidad;

        public DetalleRequest() {
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
    }
}
