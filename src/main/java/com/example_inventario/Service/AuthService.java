package com.example_inventario.Service;

import com.example_inventario.Dto.*;
import com.example_inventario.Model.Usuario;
import com.example_inventario.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthResponse registrar(RegistroRequest request) {

        Optional<Usuario> u = usuarioRepository.findByUsername(request.getUsername());
        if (u.isPresent()) {
            return new AuthResponse(
                    "Este ususario ya existe ",
                    " ",false
            );

        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());

        usuarioRepository.save(usuario);

        HashMap<String,Object> extraClaims = claims(usuario);

        String token = jwtService.generateToken(extraClaims,usuario);

        return new AuthResponse(
                "Registro exitoso",
                token, true

        );

    }

    public String autenticar(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        HashMap<String,Object> extraClaims = claims(usuario);

        return jwtService.generateToken(extraClaims,usuario);
    }

    public HashMap<String,Object> claims(Usuario usuario){
        HashMap<String,Object> extraClaims = new HashMap<>();

        extraClaims.put("roles", usuario.getRol().getDisplayName());
        extraClaims.put("username",usuario.getUsername());
        extraClaims.put("email", usuario.getEmail());
        extraClaims.put("nombre", usuario.getNombre() + " " + usuario.getApellido());

        return extraClaims;
    }
}

