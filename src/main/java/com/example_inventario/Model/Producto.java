package com.example_inventario.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name= "Producto")
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  @Column(nullable = false, length = 100)
  private String nombre;

  @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
  @Column(length = 500)
  private String descripcion;

  @NotNull(message = "el precio es obligatorio")
  @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal precio;

  @jakarta.validation.constraints.NotNull(message = "La cantidad es obligatoria")
  @Min(value = 0, message = "La cantidad no puede ser negativa")
  @Column(nullable = false)
  private Integer cantidad;

  @jakarta.validation.constraints.NotNull(message = "La categoría es obligatoria")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Categoria categoria;

  @NotNull(message = "El género es obligatorio")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Genero genero;

  @Size(max = 50, message = "La talla no puede exceder 50 caracteres")
  @Column(length = 50)
  private String talla;

  @Size(max = 50, message = "El color no puede exceder 50 caracteres")
  @Column(length = 50)
  private String color;

  @Column(name = "imagen_url")
  private String imagenUrl;

  @Column(name = "fecha_creacion", updatable = false)
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;

  // Constructores
  public Producto() {
  }

  // Métodos de ciclo de vida JPA
  @PrePersist
  protected void onCreate() {
    fechaCreacion = LocalDateTime.now();
    fechaActualizacion = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    fechaActualizacion = LocalDateTime.now();
  }
}

