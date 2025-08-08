package com.example_inventario.Controller;

import com.example_inventario.Dto.ProductoDTO;
import com.example_inventario.Model.Categoria;
import com.example_inventario.Model.Genero;
import com.example_inventario.Service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Para permitir requests desde el frontend
public class ProductoController {


    @Autowired
    private ProductoService productoService;

    /**
     * GET /api/productos - Obtener todos los productos (CLIENTE y ADMIN)
     */
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos() {
        try {
            List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/productos/{id} - Obtener producto por ID (CLIENTE y ADMIN)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);
            if (producto != null) {
                return ResponseEntity.ok(producto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/productos - Crear nuevo producto (Solo ADMIN)
     */
    @PostMapping("/crearProducto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/productos/{id} - Actualizar producto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // solo ADMIN
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {

        try {
            productoService.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(Map.of("mensaje", "Producto actualizado con exito"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al actualizar el producto"));
        }
    }


    /**
     * DELETE /api/productos/{id} - Eliminar producto (Solo ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            boolean eliminado = productoService.eliminarProducto(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    /**
     * GET /api/productos/buscar - Buscar productos con filtros (CLIENTE y ADMIN)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) Genero genero,
            @RequestParam(required = false) String nombre) {
        try {
            List<ProductoDTO> productos = productoService.buscarProductos(categoria, genero, nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/productos/categoria/{categoria} - Productos por categoría (CLIENTE y ADMIN)
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorCategoria(@PathVariable Categoria categoria) {
        try {
            List<ProductoDTO> productos = productoService.obtenerProductosPorCategoria(categoria);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/productos/genero/{genero} - Productos por género (CLIENTE y ADMIN)
     */
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorGenero(@PathVariable Genero genero) {
        try {
            List<ProductoDTO> productos = productoService.obtenerProductosPorGenero(genero);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/productos/bajo-stock - Productos con bajo stock (Solo ADMIN)
     */
    @GetMapping("/bajo-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosConBajoStock(
            @RequestParam(defaultValue = "5") Integer cantidad) {
        try {
            List<ProductoDTO> productos = productoService.obtenerProductosConBajoStock(cantidad);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/productos/sin-stock - Productos sin stock (Solo ADMIN)
     */
    @GetMapping("/sin-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosSinStock() {
        try {
            List<ProductoDTO> productos = productoService.obtenerProductosSinStock();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}


