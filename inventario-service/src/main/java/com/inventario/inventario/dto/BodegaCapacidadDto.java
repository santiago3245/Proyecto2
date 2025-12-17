package com.inventario.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodegaCapacidadDto {
    private Long bodegaId;
    private String bodegaNombre;
    private BigDecimal capacidadMaxima;
    private String unidadCapacidad;
    private Integer cantidadProductos;
    private BigDecimal porcentajeUtilizacion;
    private NivelAlerta nivelAlerta;

    public enum NivelAlerta {
        VERDE,    // < 70%
        AMARILLO, // 70% - 89%
        ROJO      // >= 90%
    }
}
