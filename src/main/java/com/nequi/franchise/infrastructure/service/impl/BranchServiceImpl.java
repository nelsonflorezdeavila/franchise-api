package com.nequi.franchise.infrastructure.service.impl;

import com.nequi.franchise.application.dto.BranchNameUpdateRequest;
import com.nequi.franchise.application.dto.BranchRequest;
import com.nequi.franchise.application.dto.BranchResponse;
import com.nequi.franchise.application.service.BranchService;
import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.repository.BranchRepository;
import com.nequi.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.infrastructure.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;
    private final BranchMapper branchMapper;

    @Override
    public Mono<BranchResponse> findById(String id) {
        log.debug("Finding branch by id: {}", id);
        return branchRepository.findById(id)
                .flatMap(this::enrichBranchWithFranchise)
                .doOnSuccess(branch -> log.debug("Found branch: {}", branch != null ? branch.id() : "null"))
                .doOnError(error -> log.error("Error finding branch by id: {}", id, error));
    }

    @Override
    public Flux<BranchResponse> findByFranchiseId(String franchiseId) {
        log.debug("Finding branches by franchise id: {}", franchiseId);
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(this::enrichBranchWithFranchise)
                .doOnComplete(() -> log.debug("Completed finding branches for franchise: {}", franchiseId))
                .doOnError(error -> log.error("Error finding branches by franchise id: {}", franchiseId, error));
    }

    @Override
    public Flux<BranchResponse> findAll() {
        log.debug("Finding all branches");
        return branchRepository.findAll()
                .flatMap(this::enrichBranchWithFranchise)
                .doOnComplete(() -> log.debug("Completed finding all branches"))
                .doOnError(error -> log.error("Error finding all branches", error));
    }

    @Override
    public Mono<BranchResponse> create(String franchiseId, BranchRequest request) {
        log.debug("Creating branch for franchise: {} with request: {}", franchiseId, request);

        return franchiseRepository.existsById(franchiseId)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found with id: " + franchiseId)))
                .then(validateUniqueBranchName(request.name(), franchiseId))
                .then(Mono.fromSupplier(() -> branchMapper.toEntity(request)))
                .map(branch -> branch.withFranchiseId(franchiseId))
                .flatMap(branchRepository::save)
                .flatMap(this::enrichBranchWithFranchise)
                .doOnSuccess(branch -> log.debug("Created branch: {}", branch.id()))
                .doOnError(error -> log.error("Error creating branch for franchise: {}", franchiseId, error));
    }

    @Override
    public Mono<BranchResponse> update(String id, BranchRequest request) {
        log.debug("Updating branch: {} with request: {}", id, request);

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found with id: " + id)))
                .flatMap(existingBranch -> validateUniqueBranchNameForUpdate(request.name(), id, existingBranch.getFranchiseId())
                        .then(Mono.just(existingBranch)))
                .doOnNext(branch -> branchMapper.updateFromRequest(request, branch))
                .flatMap(branchRepository::save)
                .flatMap(this::enrichBranchWithFranchise)
                .doOnSuccess(branch -> log.debug("Updated branch: {}", branch.id()))
                .doOnError(error -> log.error("Error updating branch: {}", id, error));
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Deleting branch: {}", id);

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found with id: " + id)))
                .flatMap(branchRepository::delete)
                .doOnSuccess(unused -> log.debug("Deleted branch: {}", id))
                .doOnError(error -> log.error("Error deleting branch: {}", id, error));
    }

    private Mono<BranchResponse> enrichBranchWithFranchise(Branch branch) {
        return franchiseRepository.findById(branch.getFranchiseId())
                .map(franchise -> branchMapper.toResponse(branch, franchise))
                .defaultIfEmpty(branchMapper.toResponse(branch));
    }

    private Mono<Void> validateUniqueBranchName(String name, String franchiseId) {
        return branchRepository.existsByNameAndFranchiseId(name, franchiseId)
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch name already exists in this franchise: " + name)))
                .then();
    }

    private Mono<Void> validateUniqueBranchNameForUpdate(String name, String branchId, String franchiseId) {
        return branchRepository.findByName(name)
                .filter(existingBranch -> !existingBranch.getId().equals(branchId) &&
                        existingBranch.getFranchiseId().equals(franchiseId))
                .hasElement()
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch name already exists in this franchise: " + name)))
                .then();
    }

    @Override
    public Mono<BranchResponse> updateName(String id, BranchNameUpdateRequest request) {
        log.debug("Updating branch name: {} with new name: {}", id, request.name());

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Branch not found with id: " + id)))
                .flatMap(existingBranch -> validateUniqueBranchNameForUpdate(request.name(), id, existingBranch.getFranchiseId())
                        .then(Mono.just(existingBranch)))
                .doOnNext(branch -> branch.setName(request.name()))
                .flatMap(branchRepository::save)
                .flatMap(this::enrichBranchWithFranchise)
                .doOnSuccess(branch -> log.debug("Updated branch name: {}", branch.id()))
                .doOnError(error -> log.error("Error updating branch name: {}", id, error));
    }
}
