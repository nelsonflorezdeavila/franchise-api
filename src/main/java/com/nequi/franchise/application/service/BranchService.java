package com.nequi.franchise.application.service;

import com.nequi.franchise.application.dto.BranchRequest;
import com.nequi.franchise.application.dto.BranchResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing branches.
 * This interface follows the Dependency Inversion Principle by depending on abstractions (DTOs)
 * rather than concrete implementations (entities).
 */
public interface BranchService {

    /**
     * Find a branch by its ID.
     *
     * @param id the branch ID
     * @return a Mono containing the branch response if found
     */
    Mono<BranchResponse> findById(String id);

    /**
     * Find all branches for a specific franchise.
     *
     * @param franchiseId the franchise ID
     * @return a Flux of branch responses
     */
    Flux<BranchResponse> findByFranchiseId(String franchiseId);

    /**
     * Find all branches.
     *
     * @return a Flux of branch responses
     */
    Flux<BranchResponse> findAll();

    /**
     * Create a new branch for a franchise.
     *
     * @param franchiseId the franchise ID
     * @param request the branch creation request
     * @return a Mono containing the created branch response
     */
    Mono<BranchResponse> create(String franchiseId, BranchRequest request);

    /**
     * Update an existing branch.
     *
     * @param id the branch ID to update
     * @param request the branch update request
     * @return a Mono containing the updated branch response
     */
    Mono<BranchResponse> update(String id, BranchRequest request);

    /**
     * Delete a branch by its ID.
     *
     * @param id the branch ID to delete
     * @return a Mono signaling completion
     */
    Mono<Void> delete(String id);
}
