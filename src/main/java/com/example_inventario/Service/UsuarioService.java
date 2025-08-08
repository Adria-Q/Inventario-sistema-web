package com.example_inventario.Service;



import com.example_inventario.Dto.UsuarioDTO;
import com.example_inventario.Mapper.UsuarioMapper;
import com.example_inventario.Model.Rol;
import com.example_inventario.Model.Usuario;
import com.example_inventario.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UsuarioService {


        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private UsuarioMapper usuarioMapper;

        @Autowired
        private PasswordEncoder passwordEncoder;


        /**
         * Obtener todos los usuarios (solo para administradores)
         */
        @Transactional(readOnly = true)
        public List<UsuarioDTO> obtenerTodosLosUsuarios() {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return usuarioMapper.toDTOList(usuarios);
        }

        /**
         * Obtener usuario por ID
         */
        @Transactional(readOnly = true)
        public UsuarioDTO obtenerUsuarioPorId(Long id) {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            return usuario.map(usuarioMapper::toDTO).orElse(null);
        }

        /**
         * Obtener usuario por username
         */
        @Transactional(readOnly = true)
        public UsuarioDTO obtenerUsuarioPorUsername(String username) {
            Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
            return usuario.map(usuarioMapper::toDTO).orElse(null);
        }

        /**
         * Obtener usuarios por rol
         */
        @Transactional(readOnly = true)
        public List<UsuarioDTO> obtenerUsuariosPorRol(Rol rol) {
            List<Usuario> usuarios = usuarioRepository.findByRolAndActivoTrue(rol);
            return usuarioMapper.toDTOList(usuarios);
        }

        /**
         * Obtener solo usuarios activos
         */
        @Transactional(readOnly = true)
        public List<UsuarioDTO> obtenerUsuariosActivos() {
            List<Usuario> usuarios = usuarioRepository.findByActivoTrue();
            return usuarioMapper.toDTOList(usuarios);
        }

        /**
         * Actualizar perfil de usuario
         */
        public UsuarioDTO actualizarPerfil(Long id, UsuarioDTO usuarioDTO) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

            if (usuarioExistente.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }

            Usuario usuario = usuarioExistente.get();

            // Validar que no se cambie el username a uno que ya existe
            if (!usuario.getUsername().equals(usuarioDTO.getUsername()) &&
                    usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
                throw new RuntimeException("El nombre de usuario ya existe");
            }

            // Validar que no se cambie el email a uno que ya existe
            if (!usuario.getEmail().equals(usuarioDTO.getEmail()) &&
                    usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }

            // Actualizar campos permitidos (NO la contraseña)
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellido(usuarioDTO.getApellido());
            usuario.setTelefono(usuarioDTO.getTelefono());

            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(usuarioActualizado);
        }

        /**
         * Cambiar rol de usuario (solo administradores)
         */
        public UsuarioDTO cambiarRol(Long id, Rol nuevoRol) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

            if (usuarioExistente.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }

            Usuario usuario = usuarioExistente.get();
            usuario.setRol(nuevoRol);

            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(usuarioActualizado);
        }

        /**
         * Activar/Desactivar usuario
         */
        public UsuarioDTO cambiarEstadoUsuario(Long id, boolean activo) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

            if (usuarioExistente.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }

            Usuario usuario = usuarioExistente.get();
            usuario.setActivo(activo);

            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(usuarioActualizado);
        }

        /**
         * Eliminar usuario
         */
        public boolean eliminarUsuario(Long id) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

            if (usuarioExistente.isEmpty()) {
                return false;
            }

            Usuario usuario = usuarioExistente.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);

            return true;
        }

        /**
         * Verificar si un usuario existe por username
         */
        @Transactional(readOnly = true)
        public boolean existeUsuarioPorUsername(String username) {
            return usuarioRepository.existsByUsername(username);
        }

        /**
         * Verificar si un usuario existe por email
         */
        @Transactional(readOnly = true)
        public boolean existeUsuarioPorEmail(String email) {
            return usuarioRepository.existsByEmail(email);
        }

        /**
         * Contar usuarios por rol
         */
        @Transactional(readOnly = true)
        public long contarUsuariosPorRol(Rol rol) {
            return usuarioRepository.findByRol(rol).size();
        }

        /**
         * Buscar usuarios por nombre o apellido
         */
        @Transactional(readOnly = true)
        public List<UsuarioDTO> buscarUsuariosPorNombre(String termino) {
            List<Usuario> usuarios = usuarioRepository.findAll()
                    .stream()
                    .filter(u -> (u.getNombre().toLowerCase().contains(termino.toLowerCase()) ||
                            u.getApellido().toLowerCase().contains(termino.toLowerCase())) &&
                            u.getActivo())
                    .toList();
            return usuarioMapper.toDTOList(usuarios);
        }
    }

