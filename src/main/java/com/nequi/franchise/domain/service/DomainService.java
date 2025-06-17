package com.nequi.franchise.domain.service;

import com.nequi.franchise.domain.model.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

/**
 * Base interface for domain services.
 * @param <T> The domain model type
 * @param <ID> The type of the identifier for the model
 */
public interface DomainService<T extends Model<ID>, ID extends Serializable> {
    Mono<T> findById(ID id);
    Flux<T> findAll();
    Mono<T> save(T entity);
    Mono<T> update(T entity);
    Mono<Void> deleteById(ID id);
}
