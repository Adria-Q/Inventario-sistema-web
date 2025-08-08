package com.example_inventario.Mapper;

import com.example_inventario.Dto.UsuarioDTO;
import com.example_inventario.Model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    /**
     * Convierte un UsuarioDTO a entidad Usuario
     * NOTA: NO incluye contraseña, debe manejarse por separado
     */
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.getActivo());

        return usuario;
    }

    /**
     * Convierte una lista de Usuarios a lista de UsuarioDTOs
     */
    public List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());

        return dto;
    }

    /**
     * Actualiza una entidad existente con datos del DTO
     * NOTA: NO actualiza contraseña, ID, fechas de creación
     */
    public void updateEntityFromDTO(UsuarioDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) {
            return;
        }

        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.getActivo());
    }
}

