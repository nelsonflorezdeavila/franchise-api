package com.nequi.franchise.application.service;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing franchises.
 * This interface follows the Dependency Inversion Principle by depending on abstractions (DTOs)
 * rather than concrete implementations (entities).
 */
public interface FranchiseService {

    /**
     * Find a franchise by its ID.
     *
     * @param id the franchise ID
     * @return a Mono containing the franchise response if found
     */
    Mono<FranchiseResponse> findById(String id);

    /**
     * Find all franchises.
     *
     * @return a Flux of franchise responses
     */
    Flux<FranchiseResponse> findAll();

    /**
     * Create a new franchise.
     *
     * @param request the franchise creation request
     * @return a Mono containing the created franchise response
     */
    Mono<FranchiseResponse> create(FranchiseRequest request);

    /**
     * Update an existing franchise.
     *
     * @param id the franchise ID to update
     * @param request the franchise update request
     * @return a Mono containing the updated franchise response
     */
    Mono<FranchiseResponse> update(String id, FranchiseRequest request);

    /**
     * Delete a franchise by its ID.
     *
     * @param id the franchise ID to delete
     * @return a Mono signaling completion
     */
    Mono<Void> delete(String id);
}
