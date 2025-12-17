package com.inventario.inventario.dto;

import com.inventario.inventario.models.entities.Inventario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDto {
    private Long id;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
    
    private String productoNombre;
    
    @NotNull(message = "El ID de la bodega es obligatorio")
    private Long bodegaId;
    
    private String bodegaNombre;
    
    private String ubicacion = "RECEPCION";
    
    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "La cantidad disponible no puede ser negativa")
    private Integer cantidadDisponible = 0;
    
    private LocalDateTime fechaUltimaEntrada;
    
    private LocalDateTime fechaUltimaSalida;

    public InventarioDto(Inventario inventario) {
        this.id = inventario.getId();
        this.productoId = inventario.getProductoId();
        this.bodegaId = inventario.getBodega().getId();
        this.bodegaNombre = inventario.getBodega().getNombre();
        this.ubicacion = inventario.getUbicacion();
        this.cantidadDisponible = inventario.getCantidadDisponible();
        this.fechaUltimaEntrada = inventario.getFechaUltimaEntrada();
        this.fechaUltimaSalida = inventario.getFechaUltimaSalida();
    }
}
