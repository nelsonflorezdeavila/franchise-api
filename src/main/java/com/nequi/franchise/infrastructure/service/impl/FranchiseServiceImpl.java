package com.nequi.franchise.infrastructure.service.impl;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import com.nequi.franchise.application.service.FranchiseService;
import com.nequi.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.infrastructure.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;

    @Override
    public Mono<FranchiseResponse> findById(String id) {
        return franchiseRepository.findById(id)
                .map(franchiseMapper::toResponse);
    }

    @Override
    public Flux<FranchiseResponse> findAll() {
        return franchiseRepository.findAll()
                .map(franchiseMapper::toResponse);
    }

    @Override
    public Mono<FranchiseResponse> create(FranchiseRequest request) {
        return Mono.just(request)
                .map(franchiseMapper::toEntity)
                .flatMap(franchiseRepository::save)
                .map(franchiseMapper::toResponse);
    }

    @Override
    public Mono<FranchiseResponse> update(String id, FranchiseRequest request) {
        return franchiseRepository.findById(id)
                .flatMap(existingFranchise -> {
                    // Update the existing franchise with new data
                    franchiseMapper.updateFromRequest(request, existingFranchise);
                    existingFranchise.setId(id); // Ensure ID is preserved
                    return franchiseRepository.save(existingFranchise);
                })
                .map(franchiseMapper::toResponse);
    }

    @Override
    public Mono<Void> delete(String id) {
        return franchiseRepository.deleteById(id);
    }

    @Override
    public Mono<FranchiseResponse> updateName(String id, String name) {
        return franchiseRepository.findById(id)
                .flatMap(existingFranchise -> {
                    existingFranchise.setName(name);
                    return franchiseRepository.save(existingFranchise);
                })
                .map(franchiseMapper::toResponse);
    }
}
