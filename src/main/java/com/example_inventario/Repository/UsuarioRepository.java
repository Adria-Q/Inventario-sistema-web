package com.example_inventario.Repository;

import com.example_inventario.Model.Usuario;
import com.example_inventario.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByActivoTrue();

    List<Usuario> findByRolAndActivoTrue(Rol rol);

    Optional<Usuario> findByUsernameOrEmail(String username, String email);
}
