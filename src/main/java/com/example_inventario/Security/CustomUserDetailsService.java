package com.example_inventario.Security;

import com.example_inventario.Model.Usuario;
import com.example_inventario.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
        private UsuarioRepository usuarioRepository;


        /**
         * Cargar usuario por username (requerido por Spring Security)
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            if (!usuario.getActivo()) {
                throw new UsernameNotFoundException("Usuario inactivo: " + username);
            }

            return usuario; // Usuario implementa UserDetails
        }

        /**
         * Cargar usuario por username o email (método personalizado)
         */
        public UserDetails loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
            Usuario usuario = usuarioRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail));

            if (!usuario.getActivo()) {
                throw new UsernameNotFoundException("Usuario inactivo: " + usernameOrEmail);
            }

            return usuario;
        }
    }