package com.nequi.franchise.infrastructure.mapper;

import com.nequi.franchise.application.dto.ProductStockRequest;
import com.nequi.franchise.application.dto.ProductStockResponse;
import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.model.ProductStock;
import com.nequi.franchise.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface ProductStockMapper {

    @Mapping(target = "id", source = "productStock.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "createdAt", source = "productStock.createdAt")
    @Mapping(target = "updatedAt", source = "productStock.updatedAt")
    ProductStockResponse toResponse(ProductStock productStock, Product product, Branch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductStock toEntity(ProductStockRequest request);

    ProductStockResponse toResponse(ProductStock productStock);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductStock updateEntity(@MappingTarget ProductStock productStock, ProductStockRequest request);
}
