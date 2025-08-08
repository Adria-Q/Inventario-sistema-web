package com.example_inventario.Dto;

import com.example_inventario.Model.Rol;
import lombok.*;

@Data
@NoArgsConstructor
public class AuthResponse {

        private String mensaje;
        private String token;
        private boolean status;


    public AuthResponse(String mensaje, String token, boolean status) {
        this.mensaje = mensaje;
        this.token = token;
        this.status = status;

    }

    }


