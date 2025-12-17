package com.inventario.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private Long id;
    private String codigo;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private String unidad;
    private String estado;
}
