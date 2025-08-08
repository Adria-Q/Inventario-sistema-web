package com.example_inventario.Dto;

import com.example_inventario.Model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDTO {


    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    private Boolean activo;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

}
