package com.example_inventario.Dto;

import com.example_inventario.Model.Categoria;
import com.example_inventario.Model.Categoria;
import com.example_inventario.Model.Genero;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

        private Long id;

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        private String nombre;

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        private String descripcion;

        @jakarta.validation.constraints.NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal precio;

        @jakarta.validation.constraints.NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        private Integer cantidad;

        @jakarta.validation.constraints.NotNull(message = "La categoría es obligatoria")
        private Categoria categoria;

        @NotNull(message = "El género es obligatorio")
        private Genero genero;

        @Size(max = 50, message = "La talla no puede exceder 50 caracteres")
        private String talla;

        @Size(max = 50, message = "El color no puede exceder 50 caracteres")
        private String color;

        private String imagenUrl;

        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaActualizacion;

}