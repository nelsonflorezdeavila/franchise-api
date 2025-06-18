package com.nequi.franchise.domain.repository;

import com.nequi.franchise.domain.model.ProductStock;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductStockRepository extends ReactiveMongoRepository<ProductStock, String> {

    Mono<ProductStock> findByProductIdAndBranchId(String productId, String branchId);

    Flux<ProductStock> findByProductId(String productId);

    Flux<ProductStock> findByBranchId(String branchId);

    @Query("{ 'stock' : { $lt : '$minStock' } }")
    Flux<ProductStock> findByStockLessThanMinStock();
}
