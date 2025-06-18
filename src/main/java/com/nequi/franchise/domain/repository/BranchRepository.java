package com.nequi.franchise.domain.repository;

import com.nequi.franchise.domain.model.Branch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends ReactiveMongoRepository<Branch, String> {

    Flux<Branch> findByFranchiseId(String franchiseId);

    Mono<Branch> findByName(String name);

    Mono<Boolean> existsByName(String name);

    Mono<Boolean> existsByNameAndFranchiseId(String name, String franchiseId);

    Mono<Long> countByFranchiseId(String franchiseId);
}
