package com.nequi.franchise.infrastructure.service.impl;

import com.nequi.franchise.application.dto.*;
import com.nequi.franchise.application.service.ProductService;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.model.ProductStock;
import com.nequi.franchise.domain.repository.BranchRepository;
import com.nequi.franchise.domain.repository.ProductRepository;
import com.nequi.franchise.domain.repository.ProductStockRepository;
import com.nequi.franchise.infrastructure.mapper.ProductMapper;
import com.nequi.franchise.infrastructure.mapper.ProductStockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final BranchRepository branchRepository;
    private final ProductMapper productMapper;
    private final ProductStockMapper productStockMapper;

    @Override
    public Mono<ProductResponse> findById(String id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse);
    }

    @Override
    public Flux<ProductResponse> findAll() {
        return productRepository.findAll()
                .map(productMapper::toResponse);
    }

    @Override
    public Flux<ProductResponse> findByCategory(String category) {
        return productRepository.findAll()
                .filter(product -> product.getCategory().equals(category))
                .map(productMapper::toResponse);
    }

    @Override
    public Mono<ProductResponse> create(ProductRequest request) {
        return productRepository.existsByName(request.name())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new RuntimeException("Product with name already exists"));
                    }
                    Product product = productMapper.toEntity(request);
                    return productRepository.save(product);
                })
                .map(productMapper::toResponse);
    }

    @Override
    public Mono<ProductResponse> update(String id, ProductRequest request) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    Product updatedProduct = productMapper.updateEntity(existingProduct, request);
                    return productRepository.save(updatedProduct);
                })
                .map(productMapper::toResponse);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productRepository.deleteById(id);
    }

    @Override
    public Mono<ProductResponse> addProductToBranch(String branchId, BranchProductRequest request) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
                .then(create(request.product()))
                .flatMap(productResponse -> {
                    ProductStock productStock = ProductStock.builder()
                            .productId(productResponse.id())
                            .branchId(branchId)
                            .stock(request.initialStock())
                            .minStock(request.minStock() != null ? request.minStock() : 0)
                            .maxStock(request.maxStock() != null ? request.maxStock() : 1000)
                            .build();

                    return productStockRepository.save(productStock)
                            .thenReturn(productResponse);
                });
    }

    @Override
    public Mono<ProductStockResponse> updateProductStock(String productId, String branchId, ProductStockRequest request) {
        return Mono.zip(
                        productRepository.findById(productId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Product not found"))),
                        branchRepository.findById(branchId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
                )
                .then(productStockRepository.findByProductIdAndBranchId(productId, branchId))
                .switchIfEmpty(Mono.error(new RuntimeException("Product stock not found for this branch")))
                .flatMap(existingStock -> {
                    ProductStock updatedStock = productStockMapper.updateEntity(existingStock, request);
                    return productStockRepository.save(updatedStock);
                })
                .flatMap(this::enrichProductStockResponse);
    }

    @Override
    public Flux<ProductStockResponse> getProductStocksByBranch(String branchId) {
        return productStockRepository.findByBranchId(branchId)
                .flatMap(this::enrichProductStockResponse);
    }

    @Override
    public Flux<ProductStockResponse> getProductStocks(String productId) {
        return productStockRepository.findByProductId(productId)
                .flatMap(this::enrichProductStockResponse);
    }

    @Override
    public Mono<ProductStockResponse> getProductStockByBranch(String productId, String branchId) {
        return productStockRepository.findByProductIdAndBranchId(productId, branchId)
                .flatMap(this::enrichProductStockResponse);
    }

    @Override
    public Flux<ProductStockResponse> getLowStockProducts() {
        return productStockRepository.findByStockLessThanMinStock()
                .flatMap(this::enrichProductStockResponse);
    }

    private Mono<ProductStockResponse> enrichProductStockResponse(ProductStock productStock) {
        return Mono.zip(
                productRepository.findById(productStock.getProductId()),
                branchRepository.findById(productStock.getBranchId())
        ).map(tuple -> productStockMapper.toResponse(productStock, tuple.getT1(), tuple.getT2()));
    }

    @Override
    public Mono<Void> removeProductFromBranch(String productId, String branchId) {
        return Mono.zip(
                        productRepository.findById(productId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Product not found"))),
                        branchRepository.findById(branchId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
                )
                .then(productStockRepository.findByProductIdAndBranchId(productId, branchId))
                .switchIfEmpty(Mono.error(new RuntimeException("Product is not associated with this branch")))
                .flatMap(productStockRepository::delete)
                .then();
    }

    @Override
    public Flux<TopStockProductByBranchResponse> getTopStockProductsByFranchise(String franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch -> productStockRepository.findByBranchId(branch.getId())
                        .flatMap(this::enrichTopStockProductResponse));
    }
    
    @Override
    public Mono<ProductResponse> updateProductName(String id, String name) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(name);
                    return productRepository.save(existingProduct);
                })
                .map(productMapper::toResponse);
    }

    private Mono<TopStockProductByBranchResponse> enrichTopStockProductResponse(ProductStock productStock) {
        return Mono.zip(
                productRepository.findById(productStock.getProductId()),
                branchRepository.findById(productStock.getBranchId())
        ).map(tuple -> new TopStockProductByBranchResponse(
                tuple.getT2().getId(),
                tuple.getT2().getName(),
                tuple.getT2().getAddress(),
                tuple.getT2().getCity(),
                tuple.getT1().getId(),
                tuple.getT1().getName(),
                tuple.getT1().getDescription(),
                tuple.getT1().getPrice(),
                tuple.getT1().getCategory(),
                productStock.getStock(),
                productStock.getMinStock(),
                productStock.getMaxStock(),
                productStock.getCreatedAt(),
                productStock.getUpdatedAt()
        ));
    }
}
