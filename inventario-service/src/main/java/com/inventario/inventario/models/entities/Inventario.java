package com.inventario.inventario.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio")
    @Column(nullable = false)
    private Long productoId;

    @NotNull(message = "La bodega es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @Column(length = 50, nullable = false)
    private String ubicacion = "RECEPCION";

    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "La cantidad disponible no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidadDisponible = 0;

    @Column(name = "fecha_ultima_entrada")
    private LocalDateTime fechaUltimaEntrada;

    @Column(name = "fecha_ultima_salida")
    private LocalDateTime fechaUltimaSalida;

    @PrePersist
    protected void onCreate() {
        if (fechaUltimaEntrada == null && cantidadDisponible > 0) {
            fechaUltimaEntrada = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Este método se puede usar para actualizar fechas según la operación
    }
}
