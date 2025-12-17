package com.inventario.inventario.dto;

import com.inventario.inventario.models.entities.Bodega;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodegaDto {
    private Long id;
    
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
    
    private String telefono;
    
    @NotNull(message = "La capacidad máxima es obligatoria")
    @Positive(message = "La capacidad máxima debe ser positiva")
    private BigDecimal capacidadMaxima;
    
    private String unidadCapacidad = "m²";
    
    private Bodega.EstadoBodega estado = Bodega.EstadoBodega.ACTIVO;

    public BodegaDto(Bodega bodega) {
        this.id = bodega.getId();
        this.codigo = bodega.getCodigo();
        this.nombre = bodega.getNombre();
        this.direccion = bodega.getDireccion();
        this.ciudad = bodega.getCiudad();
        this.telefono = bodega.getTelefono();
        this.capacidadMaxima = bodega.getCapacidadMaxima();
        this.unidadCapacidad = bodega.getUnidadCapacidad();
        this.estado = bodega.getEstado();
    }
}
