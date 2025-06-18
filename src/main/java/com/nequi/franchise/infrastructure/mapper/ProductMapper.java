package com.nequi.franchise.infrastructure.mapper;

import com.nequi.franchise.infrastructure.config.MapStructConfig;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.application.dto.ProductRequest;
import com.nequi.franchise.application.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product updateEntity(@MappingTarget Product product, ProductRequest request);
}
