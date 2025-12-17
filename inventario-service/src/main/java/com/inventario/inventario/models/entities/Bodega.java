package com.inventario.inventario.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bodegas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(length = 20)
    private String telefono;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Positive(message = "La capacidad máxima debe ser positiva")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal capacidadMaxima;

    @Column(length = 20, nullable = false)
    private String unidadCapacidad = "m²";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoBodega estado = EstadoBodega.ACTIVO;

    public enum EstadoBodega {
        ACTIVO,
        INACTIVO
    }
}
