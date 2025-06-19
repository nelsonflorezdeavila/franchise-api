package com.nequi.franchise.application.service;

import com.nequi.franchise.application.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<ProductResponse> findById(String id);

    Flux<ProductResponse> findAll();

    Flux<ProductResponse> findByCategory(String category);

    Mono<ProductResponse> create(ProductRequest request);

    Mono<ProductResponse> update(String id, ProductRequest request);

    Mono<Void> delete(String id);

    Mono<ProductResponse> addProductToBranch(String branchId, BranchProductRequest request);

    Mono<ProductStockResponse> updateProductStock(String productId, String branchId, ProductStockRequest request);

    Flux<ProductStockResponse> getProductStocksByBranch(String branchId);

    Flux<ProductStockResponse> getProductStocks(String productId);

    Mono<ProductStockResponse> getProductStockByBranch(String productId, String branchId);

    Flux<ProductStockResponse> getLowStockProducts();

    Mono<Void> removeProductFromBranch(String productId, String branchId);

    Flux<TopStockProductByBranchResponse> getTopStockProductsByFranchise(String franchiseId);

    /**
     * Updates the name of a product.
     *
     * @param id the ID of the product to update
     * @param name the new name for the product
     * @return a Mono containing the updated product response
     */
    Mono<ProductResponse> updateProductName(String id, String name);
}
