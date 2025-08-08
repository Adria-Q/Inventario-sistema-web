package com.example_inventario.Service;


import com.example_inventario.Dto.ProductoDTO;
import com.example_inventario.Mapper.ProductoMapper;
import com.example_inventario.Model.Categoria;
import com.example_inventario.Model.Genero;
import com.example_inventario.Model.Producto;
import com.example_inventario.Repository.ProductoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

        @Autowired
        private ProductoRepository productoRepository;

        @Autowired
        private ProductoMapper productoMapper;


        /**
         * Obtener todos los productos
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ProductoDTO> obtenerTodosLosProductos() {
            List<Producto> productos = productoRepository.findAllByOrderByNombreAsc();
            return productoMapper.toDTOList(productos);
        }

        /**
         * Obtener producto por ID
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public ProductoDTO obtenerProductoPorId(Long id) {
            Optional<Producto> producto = productoRepository.findById(id);
            return producto.map(productoMapper::toDTO).orElse(null);
        }

        /**
         * Crear nuevo producto
         */
        public ProductoDTO crearProducto(ProductoDTO productoDTO) {
            // Validación de negocio adicional
            validarProducto(productoDTO);

            Producto producto = productoMapper.toEntity(productoDTO);
            Producto productoGuardado = productoRepository.save(producto);
            return productoMapper.toDTO(productoGuardado);
        }

        /**
         * Actualizar producto existente
         */
        public void actualizarProducto(Long id, ProductoDTO productoDTO) {
            Optional<Producto> productoExistente = productoRepository.findById(id);

            if (productoExistente.isEmpty()) {
                throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
            }
             Producto producto = productoExistente.get();
            producto.setNombre(productoDTO.getNombre());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setDescripcion(productoDTO.getDescripcion());
            producto.setCantidad(productoDTO.getCantidad());
            producto.setCategoria(productoDTO.getCategoria());
            producto.setGenero(productoDTO.getGenero());
            producto.setTalla(productoDTO.getTalla());
            producto.setImagenUrl(productoDTO.getImagenUrl());



            // Validación de negocio
            validarProducto(productoDTO);

             productoRepository.save(producto);

        }


        /**
         * Eliminar producto
         */
        public boolean eliminarProducto(Long id) {
            if (productoRepository.existsById(id)) {
                productoRepository.deleteById(id);
                return true;
            }
            return false;
        }

        /**
         * Buscar productos por filtros
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ProductoDTO> buscarProductos(Categoria categoria, Genero genero, String nombre) {
            List<Producto> productos = productoRepository.findByMultipleFilters(categoria, genero, nombre);
            return productoMapper.toDTOList(productos);
        }

        /**
         * Obtener productos por categoría
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ProductoDTO> obtenerProductosPorCategoria(Categoria categoria) {
            List<Producto> productos = productoRepository.findByCategoria(categoria);
            return productoMapper.toDTOList(productos);
        }

        /**
         * Obtener productos por género
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ProductoDTO> obtenerProductosPorGenero(Genero genero) {
            List<Producto> productos = productoRepository.findByGenero(genero);
            return productoMapper.toDTOList(productos);
        }

        /**
         * Obtener productos con bajo stock (menos de X cantidad)
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ProductoDTO> obtenerProductosConBajoStock(Integer cantidad) {
            List<Producto> productos = productoRepository.findAllByOrderByCantidadAsc()
                    .stream()
                    .filter(p -> p.getCantidad() <= cantidad)
                    .toList();
            return productoMapper.toDTOList(productos);
        }

        /**
         * Obtener productos sin stock
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ProductoDTO> obtenerProductosSinStock() {
            List<Producto> productos = productoRepository.findByCantidad(0);
            return productoMapper.toDTOList(productos);
        }

        /**
         * Actualizar solo el stock de un producto
         */
        public ProductoDTO actualizarStock(Long id, Integer nuevaCantidad) {
            Optional<Producto> productoExistente = productoRepository.findById(id);

            if (productoExistente.isEmpty()) {
                throw new RuntimeException("Producto no encontrado con ID: " + id);
            }

            if (nuevaCantidad < 0) {
                throw new RuntimeException("La cantidad no puede ser negativa");
            }

            Producto producto = productoExistente.get();
            producto.setCantidad(nuevaCantidad);

            Producto productoActualizado = productoRepository.save(producto);
            return productoMapper.toDTO(productoActualizado);
        }

        /**
         * Contar productos por categoría
         */
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public Long contarProductosPorCategoria(Categoria categoria) {
            return productoRepository.countByCategoria(categoria);
        }

        /**
         * Validaciones de negocio para el producto
         */
        private void validarProducto(ProductoDTO producto) {
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                throw new RuntimeException("El nombre del producto es obligatorio");
            }

            if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
                throw new RuntimeException("El precio debe ser mayor a 0");
            }

            if (producto.getCantidad() == null || producto.getCantidad() < 0) {
                throw new RuntimeException("La cantidad no puede ser negativa");
            }

            if (producto.getCategoria() == null) {
                throw new RuntimeException("La categoría es obligatoria");
            }

            if (producto.getGenero() == null) {
                throw new RuntimeException("El género es obligatorio");
            }
        }
    }

