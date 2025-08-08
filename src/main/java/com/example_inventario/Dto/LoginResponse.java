package com.example_inventario.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

        //resppuesta
        private boolean success;
        private String message;
        private String token;

        public LoginResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
            this.token = null;
        }

        public LoginResponse(String token) {
            this.success = true;
            this.message = "Login exitoso";
            this.token = token;
        }



    }

