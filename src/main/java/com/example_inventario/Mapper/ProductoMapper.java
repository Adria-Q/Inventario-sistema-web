package com.example_inventario.Mapper;



import com.example_inventario.Dto.ProductoDTO;
import com.example_inventario.Model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    Producto toEntity(ProductoDTO dto);

    ProductoDTO toDTO(Producto producto);

    List<ProductoDTO> toDTOList(List<Producto> productos);

    void updateEntityFromDTO(ProductoDTO dto, @MappingTarget Producto producto);
}
