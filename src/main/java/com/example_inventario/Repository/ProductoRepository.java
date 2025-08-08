package com.example_inventario.Repository;

import com.example_inventario.Model.Categoria;
import com.example_inventario.Model.Genero;
import com.example_inventario.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository <Producto, Long> {
    // Buscar por categoría
    List<Producto> findByCategoria(Categoria categoria);

    // Buscar por género
    List<Producto> findByGenero(Genero genero);

    // Buscar por categoría y género
    List<Producto> findByCategoriaAndGenero(Categoria categoria, Genero genero);

    // Buscar por nombre que contenga texto (ignora mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos con stock mayor a 0
    List<Producto> findByCantidadGreaterThan(Integer cantidad);

    // Buscar productos sin stock
    List<Producto> findByCantidad(Integer cantidad);

    // Buscar por múltiples criterios con consulta personalizada
    @Query("SELECT p FROM Producto p WHERE " +
            "(:categoria IS NULL OR p.categoria = :categoria) AND " +
            "(:genero IS NULL OR p.genero = :genero) AND " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<Producto> findByMultipleFilters(@Param("categoria") Categoria categoria,
                                         @Param("genero") Genero genero,
                                         @Param("nombre") String nombre);

    // Contar productos por categoría
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria = :categoria")
    Long countByCategoria(@Param("categoria") Categoria categoria);

    // Obtener productos ordenados por nombre
    List<Producto> findAllByOrderByNombreAsc();

    // Obtener productos ordenados por cantidad (menos stock primero)
    List<Producto> findAllByOrderByCantidadAsc();
}
