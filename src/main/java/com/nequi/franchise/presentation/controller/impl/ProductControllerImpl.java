package com.nequi.franchise.presentation.controller.impl;

import com.nequi.franchise.application.dto.*;
import com.nequi.franchise.application.service.ProductService;
import com.nequi.franchise.infrastructure.dto.ApiResponse;
import com.nequi.franchise.infrastructure.utils.CustomResponseBuilder;
import com.nequi.franchise.presentation.controller.ProductController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final CustomResponseBuilder customResponseBuilder;

    @Override
    @GetMapping("/products/{id}")
    public Mono<ResponseEntity<ApiResponse<ProductResponse>>> getProduct(@PathVariable String id) {
        return productService.findById(id)
                .map(customResponseBuilder::success)
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @GetMapping("/products")
    public Mono<ResponseEntity<ApiResponse<List<ProductResponse>>>> getAllProducts(String category) {
        return (category != null ? productService.findByCategory(category) : productService.findAll())
                .collectList()
                .map(customResponseBuilder::success);
    }

    @Override
    @PostMapping("/products")
    public Mono<ResponseEntity<ApiResponse<ProductResponse>>> createProduct(ProductRequest request) {
        return productService.create(request)
                .map(product -> customResponseBuilder.created(product, "product.created"));
    }

    @Override
    @PostMapping("/branches/{branchId}/products")
    public Mono<ResponseEntity<ApiResponse<ProductResponse>>> addProductToBranch(@PathVariable String branchId, BranchProductRequest request) {
        return productService.addProductToBranch(branchId, request)
                .map(product -> customResponseBuilder.created(product, "product.added.to.branch"));
    }

    @Override
    @PatchMapping("/products/{productId}/branches/{branchId}/stock")
    public Mono<ResponseEntity<ApiResponse<ProductStockResponse>>> updateProductStock(@PathVariable String productId, @PathVariable String branchId, ProductStockRequest request) {
        return productService.updateProductStock(productId, branchId, request)
                .map(stock -> customResponseBuilder.success(stock, "product.stock.updated"))
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @GetMapping("/branches/{branchId}/products")
    public Mono<ResponseEntity<ApiResponse<List<ProductStockResponse>>>> getProductStocksByBranch(@PathVariable String branchId) {
        return productService.getProductStocksByBranch(branchId)
                .collectList()
                .map(customResponseBuilder::success);
    }

    @Override
    @GetMapping("/products/{productId}/branches/{branchId}/stock")
    public Mono<ResponseEntity<ApiResponse<ProductStockResponse>>> getProductStock(@PathVariable String productId, @PathVariable String branchId) {
        return productService.getProductStockByBranch(productId, branchId)
                .map(customResponseBuilder::success)
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @DeleteMapping("/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<ApiResponse<Void>>> removeProductFromBranch(@PathVariable String branchId, @PathVariable String productId) {
        return productService.removeProductFromBranch(productId, branchId)
                .then(Mono.just(customResponseBuilder.deleted("product.removed.from.branch")));
    }
}
