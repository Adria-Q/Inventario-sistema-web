package com.example_inventario.Controller;

import com.example_inventario.Dto.*;
import com.example_inventario.Service.AuthService;
import com.example_inventario.Service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    /**
     * POST /api/auth/registro - Registrar nuevo usuario
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            AuthResponse response = authService.registrar(request); // 👈 aquí estaba el error
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Aquí llamas a tu servicio que autentica el usuario
            String token = authService.autenticar(request); // Este metodo devolvera un jwt exitoso

            // Si el token se genera correctamente, devolvemos la respuesta
            LoginResponse response = new LoginResponse(token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Manejo de errores por credenciales inválidas
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            // Otros errores no controlados
            ErrorResponse error = new ErrorResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
