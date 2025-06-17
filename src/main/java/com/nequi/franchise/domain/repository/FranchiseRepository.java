package com.nequi.franchise.domain.repository;

import com.nequi.franchise.domain.model.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {
    Mono<Franchise> findByName(String name);
}
