package com.example_inventario.Security;

import com.example_inventario.Dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
    public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {

            // Configurar la respuesta
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Crear mensaje de error
            ErrorResponse errorResponse = new ErrorResponse(
                    "Acceso denegado. Token de autenticación requerido o inválido.",
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            // Convertir a JSON y escribir en la respuesta
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), errorResponse);
        }
    }
