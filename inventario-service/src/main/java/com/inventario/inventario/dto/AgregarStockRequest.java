package com.inventario.inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarStockRequest {
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
    
    @NotNull(message = "El ID de la bodega es obligatorio")
    private Long bodegaId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
    
    private String ubicacion = "RECEPCION";
}
